package com.example.camel_sql.service;

import org.springframework.web.multipart.MultipartFile;

public interface MQProducerService {

    void processAndSendFile(MultipartFile file) throws Exception;

}
