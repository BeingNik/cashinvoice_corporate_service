package com.example.camel_sql.service.impl;

import com.example.camel_sql.entity.SwiftMessageEntity;
import com.example.camel_sql.parser.SwiftEntityParser;
import com.example.camel_sql.repository.SwiftMessageEntityRepository;
import com.example.camel_sql.service.DataReceiverService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class DataReceiverServiceImpl implements DataReceiverService {

    @Autowired
    private SwiftMessageEntityRepository swiftMessageEntityRepository;

    @Autowired
    private SwiftEntityParser swiftEntityParser;

    private static final Logger logger = LoggerFactory.getLogger(DataReceiverServiceImpl.class);

    @Override
    public String saveData(String swiftMessageContent) {
        try {
            SwiftMessageEntity swiftMessageEntity = swiftEntityParser.parse(swiftMessageContent);

            swiftMessageEntityRepository.save(swiftMessageEntity);

            return "Data received and saved.";
        } catch (Exception e) {

            logger.error("Error saving data: {}", e.getMessage());

            return "Data saving failed: " + e.getMessage();
        }
    }
}
