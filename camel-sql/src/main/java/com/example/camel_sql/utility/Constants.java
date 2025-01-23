package com.example.camel_sql.utility;

public class Constants {

    public static final String STATUS_PROCESSING = "PROCESSING";
    public static final String STATUS_SUCCESS = "SUCCESS";
    public static final String STATUS_ERROR = "UNKNOWN_ERROR";
    public static final String TIMESTAMP_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final String INCOMING = "incoming";
    public static final String OUTGOING = "outgoing";
    public static final String RTF = "rtf";
    public static final String TXT = "txt";

    public static final String ROUTER_CONFIGURATION_STARTED = "Router configuration started at ";
    public static final String ROUTER_CONFIGURATION_ENDED = "Router configuration ended at ";

    public static final String FILE_PROCESS_STARTED = "File processing started at ";
    public static final String FILE_PROCESS_ENDED = "File processing ended at ";
    public static final String FILE_STORAGE_CREATION_STARTED = "FileStorage creation started at ";
    public static final String FILE_STORAGE_CREATION_COMPLETED = "FileStorage creation completed at ";
    public static final String FILE_STORAGE_STATUS_UPDATE_STARTED = "FileStorage status update started at ";
    public static final String FILE_STORAGE_STATUS_UPDATE_COMPLETED = "FileStorage status update completed at ";
    public static final String FILE_STORAGE_CREATED = "FileStorage entry created with ID at ";

    public static final String CONTENT_PROCESS_STARTED = "Content processing started at ";
    public static final String ACTIVEMQ_PROCESSING_STARTED = "ActiveMQ message processing started at ";
    public static final String ACTIVEMQ_PROCESSING_COMPLETED = "ActiveMQ message processing setup completed at ";
    public static final String ACTIVEMQ_MESSAGE_PROCESSING_COMPLETED = "Processing ActiveMQ message completed at ";

    //Channel Types
    public static final String FILE = "FILE";
    public static final String SFTP = "SFTP";
    public static final String MQ = "MQ";
    public static final String API_CALL = "API_CALL";
    public static final String CHANNEL_TYPE = "channelType" ;
    public static final String FILE_STORAGE_ID = "fileStorageId";
    // apache component Types
    public static final String FILE_COMPONENT = "file";
    public static final String SFTP_COMPONENT = "sftp";
    public static final String MQ_COMPONENT = "mq";

}
