package com.example.camel_sql.route;

import com.example.camel_sql.entity.FileStorage;
import com.example.camel_sql.entity.RouteConfig;
import com.example.camel_sql.exception.MTMessageException;
import com.example.camel_sql.repository.FileStorageRepository;
import com.example.camel_sql.repository.RouteConfigRepository;
import com.example.camel_sql.utility.Constants;
import com.example.camel_sql.validator.ValidationService;
import com.prowidesoftware.swift.model.MtSwiftMessage;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.file.GenericFile;
import org.apache.camel.http.base.HttpOperationFailedException;
import org.apache.tika.Tika;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

import static com.example.camel_sql.utility.Constants.*;
import static com.example.camel_sql.utility.FileUtils.getFileExtension;
import static com.example.camel_sql.utility.TimestampUtils.getTimestamp;
import static org.apache.camel.Exchange.CONTENT_TYPE;

@Component
public class IncomingRouter extends RouteBuilder {

    private static final Logger logger = LoggerFactory.getLogger(IncomingRouter.class);

    @Autowired
    private FileStorageRepository fileStorageRepository;

    @Autowired
    private RouteConfigRepository routeConfigRepository;

    @Autowired
    private ValidationService validationService;

    @Value("${data.receiver.url}")
    private String dataReceiverUrl;

    @Value("${bank.data.receiver.url}")
    private String bankDataReceiverUrl;

    @Value("${secret.key.path}")
    private String secretKeyPath;


    @Override
    public void configure() throws Exception {
        try {
                List<RouteConfig> routeConfigs = routeConfigRepository.findAll();

            List<RouteConfig> incomingRouteConfigs = routeConfigs.stream()
                    .filter(routeConfig -> INCOMING.equals(routeConfig.getSourceEndpointType()))
                    .collect(Collectors.toList());

            if (incomingRouteConfigs == null || routeConfigs.isEmpty()) {
                logger.warn("No source-destination information found.");
                return;
            }

            logger.info(Constants.ROUTER_CONFIGURATION_STARTED + "{}", getTimestamp());

            for (RouteConfig config : incomingRouteConfigs) {
                if (config != null) {
                    if (FILE.equalsIgnoreCase(config.getSourceChannelType())) {
                        processFileInputs(config.getSourcePath(), config.getDestinationPath());
//                    } else if (MQ.equalsIgnoreCase(config.getSourceChannelType())) {
//                        processActiveMQMessages(config.getSourcePath());
                    } else if (SFTP.equalsIgnoreCase(config.getSourceChannelType())) {
                        processSFTPMessage(config);
                    } else {
                        logger.warn("Unknown type for config: {}", config);
                    }
                }
            }
            logger.info(Constants.ROUTER_CONFIGURATION_ENDED + "{}", getTimestamp());
        } catch (Exception e) {
            logger.error("Error occurred during FileRouter configuration: {}", e.getMessage(), e);
            throw e;
        }
    }

    private void processSFTPMessage(RouteConfig config) {
        String destination = null;
        String destinatioComponet = null;
        String srcPwd = config.getSourcePassword().replace("@", "%40");

//        String sourceSmtpUri = config.getSourceUsername() + ":" + srcPwd + "@" + config.getSourceHost() + ":" +
//                config.getSourcePort() + config.getSourcePath() +
//                "?delete=true&include=.*\\.rtf&maxMessagesPerPoll=3&disconnect=true&useUserKnownHostsFile=false";
         String sourceSmtpUri = config.getSourceUsername() + ":" + srcPwd + "@" + config.getSourceHost() + ":" +
                config.getSourcePort() + config.getSourcePath() +
                "?delete=true&include=.*\\.rtf&maxMessagesPerPoll=3&disconnect=true&useUserKnownHostsFile=true&KnownHostsFile=~/.ssh/known_hosts";

        logger.info("smtpUrl :{} ",sourceSmtpUri);

        if (FILE.equals(config.getDestinationChannelType() )){
            destination = config.getDestinationPath();
            destinatioComponet = FILE_COMPONENT;
        } else if (SFTP.equals(config.getDestinationChannelType())) {
            String destPwd = config.getSourcePassword().replace("@", "%40");

            destination = config.getDestinationUsername() + ":" + destPwd + "@" + config.getDestinationHost() + ":" +
                    config.getDestinationPort() + config.getDestinationPath() +
                "?delete=true&include=.*\\.rtf&maxMessagesPerPoll=3&disconnect=true&useUserKnownHostsFile=false";

            destinatioComponet = SFTP_COMPONENT;
        }

        from(SFTP_COMPONENT+ "://" + sourceSmtpUri)
                .setHeader(CHANNEL_TYPE, constant(SFTP))
                .log("Processing file from folder: ${file:name}")
                .process(this::processFile)
                .to(dataReceiverUrl)
                .log("JSON sent to REST endpoint from file")
                .process(this::updateFileStorageStatus)
                .to(destinatioComponet+ "://" + destination)
                .log("File moved to destination directory");
    }

    private void processFileInputs(String source, String destination) {
            from("file://" + source)
                    .setHeader(CHANNEL_TYPE, constant(FILE))
                    .log("Processing file from folder: ${file:name}")
                    .process(this::processFile)
                    .log("exception occurred at time ")
                    .end()
                    .to(dataReceiverUrl)
                    .log("JSON sent to REST endpoint from file")
                    .process(this::updateFileStorageStatus)
                    .to(bankDataReceiverUrl)
                    .to("file://" + destination)
                    .log("File moved to destination directory");
    }

    private void processActiveMQMessages(String srcQueue) {

        from("activemq:queue:"+srcQueue)
                .log("Processing message from ActiveMQ: ${body}")
                .process(this::processMessage)
                .doTry()
                .to(dataReceiverUrl)
                .log("JSON sent to REST endpoint from ActiveMQ")
                .process(this::updateFileStorageStatus)
                .doCatch(Exception.class)
                .process(exchange -> {
                    Throwable exception = exchange.getProperty(Exchange.EXCEPTION_CAUGHT, Throwable.class);
                    if (exception instanceof org.apache.camel.http.base.HttpOperationFailedException) {
                        HttpOperationFailedException httpException = (HttpOperationFailedException) exception;
                        int statusCode = httpException.getStatusCode();
                        exchange.getIn().setHeader("HTTP_ERROR_CODE", statusCode);
                        exchange.getIn().setBody("HTTP error: " + statusCode + " - " + httpException.getMessage());
                    }

                    Long fileStorageId = exchange.getIn().getHeader("fileStorageId", Long.class);
                    FileStorage fileStorage = fileStorageRepository.findById(fileStorageId)
                            .orElseThrow(() -> new RuntimeException("FileStorage not found"));

                    fileStorage.setStatus("Failed");
                    fileStorageRepository.save(fileStorage);
                    exchange.getIn().setBody("Handled 500 error gracefully.");
                })
                .log("Exception caught and handled: ${exception.message}")
                .end();

        logger.info(ACTIVEMQ_PROCESSING_COMPLETED + "{}", getTimestamp());

    }

    private void processFile(Exchange exchange) throws Exception {
        logger.info("Processing file: {}", exchange.getIn().getHeader(Exchange.FILE_NAME));
        logger.info(FILE_PROCESS_STARTED + "{}", getTimestamp());

        String fileName = ((GenericFile<?>) exchange.getIn().getBody()).getFileName();
        String fileContents = exchange.getIn().getBody(String.class);
        String content = null;
        String channelType = exchange.getIn().getHeader("channelType", String.class);

        if(RTF.equals(getFileExtension(fileName))){
            Tika tika = new Tika();
            content = tika.parseToString(new ByteArrayInputStream(fileContents.getBytes(StandardCharsets.UTF_8)));

        } else if (TXT.equals(getFileExtension(fileName))) {
            content = fileContents.trim();
        }

        handleContentProcessing(exchange, fileName, content, channelType);

        logger.info(FILE_PROCESS_ENDED + "{}", getTimestamp());
    }

    private void processMessage(Exchange exchange)  {
        logger.info(ACTIVEMQ_PROCESSING_STARTED + "{}", getTimestamp());

        String content = exchange.getIn().getBody(String.class);
        handleContentProcessing(exchange, "ActiveMQMessage", content, "MQ");

        logger.info(ACTIVEMQ_MESSAGE_PROCESSING_COMPLETED + "{}", getTimestamp());
    }

    private void handleContentProcessing(Exchange exchange, String source, String content, String channelType){

        SecretKey secretKey;
        logger.info("Started content processing from source: {}", source);
        logger.info(CONTENT_PROCESS_STARTED + "{}", getTimestamp());

        MtSwiftMessage mtSwiftMessage = MtSwiftMessage.parse(content);
//        String jsonData = mtSwiftMessage.toJson();
        String messageType = mtSwiftMessage.getMessageType();
        String jsonData;

        switch (messageType) {
            case "300":
            case "305":
                jsonData = validationService.validate(mtSwiftMessage);
                break;

            case "103":
                jsonData = mtSwiftMessage.toJson();
                logger.info("Json data for 103: {}", jsonData);
                break;

            default:
                jsonData = mtSwiftMessage.toJson();
                logger.info("Json data : {}", jsonData);
                break;
        }

        logger.info("Json data : {}",jsonData);

        exchange.getIn().setBody(mtSwiftMessage.getMessage());

        //load the Secret Key
        try{
            secretKey = loadKeyFromFile(secretKeyPath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        logger.info("Secret key loaded from file for source: {}", source);

        String encryptedJson = encryptString(jsonData, secretKey);
        logger.info("JSON encrypted for source: {}", source);

        FileStorage fileStorage = createFileStorage(source, channelType, encryptedJson, jsonData);
        fileStorageRepository.save(fileStorage);

        exchange.getIn().setHeader("fileStorageId", fileStorage.getId());
        exchange.getIn().setHeader(CONTENT_TYPE, "application/json");
        logger.info(FILE_STORAGE_CREATED + "{}", getTimestamp());
    }

    private void updateFileStorageStatus(Exchange exchange) {
        logger.info(FILE_STORAGE_STATUS_UPDATE_STARTED + "{}", getTimestamp());

        int responseCode = exchange.getIn().getHeader(Exchange.HTTP_RESPONSE_CODE, Integer.class);
        Long fileStorageId = exchange.getIn().getHeader(FILE_STORAGE_ID, Long.class);

        logger.info("Updating file storage status for FileStorage ID: {}", fileStorageId);

        FileStorage fileStorage = fileStorageRepository.findById(fileStorageId)
                .orElseThrow(() -> {
                    logger.error("FileStorage with ID {} not found", fileStorageId);
                    return new RuntimeException("FileStorage not found");
                });

        if (responseCode >= 200 && responseCode < 300) {
            fileStorage.setStatus(Constants.STATUS_SUCCESS);
            fileStorage.setEndTime(LocalDateTime.now());
            logger.info("File processed successfully with status: {}", Constants.STATUS_SUCCESS);
        } else {
            fileStorage.setStatus(Constants.STATUS_ERROR);
            fileStorage.setEndTime(LocalDateTime.now());
            logger.error("Error occurred during file processing with status: {}", Constants.STATUS_ERROR);
        }
        fileStorageRepository.save(fileStorage);

        logger.info(FILE_STORAGE_STATUS_UPDATE_COMPLETED + "{}", getTimestamp());
    }

    private FileStorage createFileStorage(String source, String channelType, String encryptedJson, String json) {

        logger.info(FILE_STORAGE_CREATION_STARTED + "{}", getTimestamp());

        FileStorage fileStorage = new FileStorage();
        fileStorage.setFileName(source);
        fileStorage.setFileType(getFileExtension(source));
        fileStorage.setStartTime(LocalDateTime.now());
        fileStorage.setStatus(Constants.STATUS_PROCESSING);
        fileStorage.setChannelType(channelType);
        fileStorage.setJsonData(json);
        fileStorage.setEncryptedData(encryptedJson);

        logger.info(FILE_STORAGE_CREATION_COMPLETED + "{}", getTimestamp());

        return fileStorage;
    }

    private SecretKey loadKeyFromFile(String filePath) throws IOException {
        try {
            byte[] keyBytes = Files.readAllBytes(Paths.get(filePath));
            logger.info("Secret key loaded successfully from file: {}", filePath);
            return new SecretKeySpec(keyBytes, 0, keyBytes.length, "AES");
        } catch (IOException e) {
            logger.error("Error loading secret key from file: {}", filePath, e);
            throw e;
        }
    }

    private String encryptString(String strToEncrypt, SecretKey secretKey) {
        try {
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            byte[] encryptedBytes = cipher.doFinal(strToEncrypt.getBytes());
            String encryptedString = Base64.getEncoder().encodeToString(encryptedBytes);
            logger.info("String encrypted successfully.");
            return encryptedString;
        } catch (Exception e) {
            logger.error("Error encrypting string.", e);
        }
        return null;
    }
}
