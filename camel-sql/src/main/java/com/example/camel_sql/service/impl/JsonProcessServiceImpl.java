package com.example.camel_sql.service.impl;

import com.example.camel_sql.parser.MTMessageParser;
import com.example.camel_sql.parser.MXMessageParser;
import com.example.camel_sql.service.JsonProcessService;
import com.example.camel_sql.validation.JsonBodyValidator;
import com.example.camel_sql.validation.JsonHeaderValidator;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.rtf.RTFEditorKit;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class JsonProcessServiceImpl implements JsonProcessService {

    @Autowired
    private JsonHeaderValidator jsonHeaderValidator;

    @Autowired
    private JsonBodyValidator jsonBodyValidator;

    @Autowired
    private MTMessageParser mtMessageParser;

    @Autowired
    private MXMessageParser mxMessageParser;

    @Value("${outgoing.mt-file.output.directory}")
    private String outputDirectory;

    Logger logger = LoggerFactory.getLogger(JsonProcessServiceImpl.class);

    @Override
    public byte[] processFile(MultipartFile file) throws IOException {
        if (null != file) {
            String content = new String(file.getBytes());
            if (jsonHeaderValidator.isValidHeader(content) && jsonBodyValidator.isValidBody(content)) {
                logger.info("Json File Content : {}", content);

                byte[] data = convertJsonToFile(file);

                if (null != data) {
                    writeToFile(data, outputDirectory, file.getOriginalFilename());
                    logger.info("File added to the given directory : {}", outputDirectory);
                }
                return data;
            }
        }
        return null;
    }

    private byte[] convertJsonToFile(MultipartFile file){
        try {
            String fileName = file.getOriginalFilename() != null ? file.getOriginalFilename() : "";
            byte[] content = null;
//            node = new ObjectMapper().readTree(file.getInputStream());
            String contentStr;
            if (fileName.endsWith(".rtf")) {
                contentStr = extractTextFromRTF(file.getBytes());
            } else {
                contentStr = new String(file.getBytes());
            }
            JsonNode node = new ObjectMapper().readTree(contentStr);

            //Refactor later --need to be changed
            if (fileName.startsWith("MT")) {
                return mtMessageParser.convertMTMessage(node);
            } else if (fileName.startsWith("MX")) {
                return mxMessageParser.convertMXMessage(node);
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        catch (BadLocationException e) {
            throw new RuntimeException(e);
        }
        return null;

    }

    private void writeToFile(byte[] data, String outputDirectory, String fileName) throws IOException {

        Path outputPath = Paths.get(outputDirectory, fileName.replaceFirst("[.][^.]+$", "") + ".txt");
        Files.createDirectories(outputPath.getParent());
        Files.write(outputPath, data);

        logger.info("Data successfully written to file: {}", outputPath.toString());
    }

    private String extractTextFromRTF(byte[] rtfBytes) throws IOException, BadLocationException {
        RTFEditorKit rtfParser = new RTFEditorKit();
        Document document = rtfParser.createDefaultDocument();
        rtfParser.read(new ByteArrayInputStream(rtfBytes), document, 0);
        return document.getText(0, document.getLength());
    }

}
