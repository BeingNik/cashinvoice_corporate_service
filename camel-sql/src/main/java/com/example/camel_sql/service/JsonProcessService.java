package com.example.camel_sql.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface JsonProcessService {

    byte[] processFile(MultipartFile file) throws IOException;
}
