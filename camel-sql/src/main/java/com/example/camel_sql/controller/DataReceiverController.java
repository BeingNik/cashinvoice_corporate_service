package com.example.camel_sql.controller;

import com.example.camel_sql.service.DataReceiverService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/receiveData")
public class DataReceiverController {

    @Autowired
    private DataReceiverService dataReceiverService;

    @PostMapping()
    public ResponseEntity<String> receiveData(@RequestBody String messageContent) {
        String responseMessage = dataReceiverService.saveData(messageContent);

        if (responseMessage.contains("failed")) {
            return ResponseEntity.status(500).body(responseMessage);
        }

        return ResponseEntity.ok(responseMessage);
    }
}
