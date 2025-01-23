package com.example.camel_sql.controller;

import com.example.camel_sql.service.MQProducerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/mq")
public class MQProducerController {

    @Autowired
    private MQProducerService mqProducerService;

    @PostMapping("/add-to-queue")
    public ResponseEntity<String> addFileToMQ(@RequestParam("file") MultipartFile file) {
        try {
            mqProducerService.processAndSendFile(file);
            return new ResponseEntity<>("File processed and added to queue successfully.",
                    HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Failed to process file: " + e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
