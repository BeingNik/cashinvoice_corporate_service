package com.example.camel_sql.controller;

import com.example.camel_sql.service.JsonProcessService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class JsonProcessController {

    @Autowired
    private JsonProcessService jsonProcessService;

    Logger logger = LoggerFactory.getLogger(JsonProcessController.class);
    @PostMapping("/processJson")
    public ResponseEntity<String> processJson(@RequestParam("file")MultipartFile file){
        try{

            logger.info("Processing started for file : {}",file.getOriginalFilename());

            byte[] data = jsonProcessService.processFile(file);

            logger.info("processed Successfully : {}",data);

            return ResponseEntity.ok("File processed successfully!");
        }
        catch (Exception e){
            return ResponseEntity.internalServerError()
                    .body("Error processing file: " + e.getMessage());
        }

    }

}
