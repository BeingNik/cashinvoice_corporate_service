package com.example.camel_sql.parser;

import com.example.camel_sql.entity.SwiftMessageEntity;
import com.prowidesoftware.swift.model.MtSwiftMessage;
import com.prowidesoftware.swift.model.SwiftBlock1;
import com.prowidesoftware.swift.model.SwiftBlock2Input;
import org.springframework.stereotype.Service;

@Service
public class SwiftEntityParser {

    public SwiftMessageEntity parse(String content) {
//        logger.info(SWIFT_MESSAGE_ENTITY_PARSING_STARTED + "{}", getTimestamp());

        SwiftMessageEntity swiftMessageEntity = new SwiftMessageEntity();
        MtSwiftMessage msg = MtSwiftMessage.parse(content);
        if (msg != null && msg.modelMessage() != null) {
            // Parse Block 1
            SwiftBlock1 block1 = msg.modelMessage().getBlock1();
            if (block1 != null) {
                swiftMessageEntity.setApplicationId(block1.getApplicationId());
                swiftMessageEntity.setServiceId(block1.getServiceId());
                swiftMessageEntity.setLogicalTerminal(block1.getLogicalTerminal());
                swiftMessageEntity.setSessionNumber(block1.getSessionNumber());
                swiftMessageEntity.setSequenceNumber(block1.getSequenceNumber());
            }

            // Parse Block 2
            SwiftBlock2Input block2 = (SwiftBlock2Input) msg.modelMessage().getBlock2();
            if (block2 != null) {
                swiftMessageEntity.setMessageType(block2.getMessageType());
                swiftMessageEntity.setReceiverAddress(block2.getReceiverAddress());
                swiftMessageEntity.setMessagePriority(block2.getMessagePriority());
                swiftMessageEntity.setDeliveryMonitoring(block2.getDeliveryMonitoring());
            }

            // Parse Block 4 (Example for additional fields)
//            SwiftBlock4 block4 = msg.modelMessage().getBlock4();
//            if (block4 != null) {
//                swiftMessageEntity.setMessageUserReference(block4.getTagValue("20")); // Example for Tag 20
//                swiftMessageEntity.setBankingPriority(block4.getTagValue("26E"));    // Example for Tag 26E
//                // Add more field mappings as needed
//            }
        }

//        logger.info(SWIFT_MESSAGE_ENTITY_PARSING_COMPLETED + "{}", getTimestamp());
        return swiftMessageEntity;
    }

}
