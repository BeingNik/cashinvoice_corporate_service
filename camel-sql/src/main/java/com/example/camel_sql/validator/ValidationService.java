package com.example.camel_sql.validator;

import com.example.camel_sql.exception.MTMessageException;
import com.prowidesoftware.swift.model.MtSwiftMessage;
import com.prowidesoftware.swift.model.SwiftBlock4;
import com.prowidesoftware.swift.model.SwiftMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.Map;

@Component
public class ValidationService {

    @Autowired
    private MTValidatorService mtValidatorService;

    public String validate(MtSwiftMessage mtSwiftMessage) {
        try {
            StringBuilder errorMessages = new StringBuilder();
            SwiftMessage swiftMessage = mtSwiftMessage.modelMessage();
            mtValidatorService.validateMTFile(swiftMessage , errorMessages);
            if (!errorMessages.isEmpty())
            {
                throw new MTMessageException(errorMessages.toString());
//                ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                        .body(errorMessages);
            }

            return swiftMessage.toJson();
//            Map<String, Object> objectMap =  transformSwiftMessage(swiftMessage);
//            return objectMap.toString();
//            String jsonResponse = swiftMessage.toJson();

//            MtSwiftMessage mtSwiftMessage = new MtSwiftMessage(swiftMessage);
//          String res =   mtSwiftMessage.toJson();
//            parseMTSwiftMessageFile(mtSwiftMessage);

//            ResponseEntity.ok(objectMap);

        } catch (Exception e) {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error processing the MT300 message: " + e.getMessage());
        }
        return null;
    }

    public Map<String, Object> transformSwiftMessage(SwiftMessage swiftMessage) {
        Map<String, Object> response = new LinkedHashMap<>();

        // Extract blocks
        String block1 = swiftMessage.getBlock1().getBlockValue();
        String block2 = swiftMessage.getBlock2().getBlockValue();
        SwiftBlock4 block4 = swiftMessage.getBlock4();

        // Header transformation
        Map<String, String> header = new LinkedHashMap<>();
        header.put("block1", block1);
        header.put("block2", block2);

        // Body transformation
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("transactionReferenceNumber", block4.getTagValue("20")); // Tag 20: Transaction Reference
        body.put("relatedReference", block4.getTagValue("21")); // Tag 21: Related Reference
        body.put("accountIdentification", block4.getTagValue("82A")); // Tag 82A: Account Identification

        // Add serviceLevel dynamically if tag 22A exists
        String serviceLevel = block4.getTagValue("22A"); // Tag 22A: Type of Operation
        if (serviceLevel != null) {
            body.put("serviceLevel", serviceLevel);
        }

        // Add sequenceNumber only if relevant
        String sequenceNumber = block4.getTagValue("22C"); // Example: Tag 22C might indicate sequence/order
        if (sequenceNumber != null) {
            body.put("sequenceNumber", sequenceNumber);
        }

        // Extract sender information from block1
        Map<String, String> sender = new LinkedHashMap<>();
        if (block1 != null && block1.length() >= 12) {
            sender.put("bank", block1.substring(3, 11)); // Extract bank code from block1
            sender.put("location", block1.substring(11, 15)); // Extract location from block1
        }
        body.put("sender", sender);

        // Extract receiver information from block2
        Map<String, String> receiver = new LinkedHashMap<>();
        if (block2 != null && block2.length() >= 12) {
            receiver.put("bank", block2.substring(3, 11)); // Extract bank code from block2
            receiver.put("location", block2.substring(11, 15)); // Extract location from block2
        }
        body.put("receiver", receiver);

        // Creditor account details
        Map<String, String> creditorAccount = new LinkedHashMap<>();
        String tag87A = block4.getTagValue("87A");
        if (tag87A != null) {
            creditorAccount.put("accountNumber", tag87A); // Tag 87A: Creditor account number
            creditorAccount.put("accountName", ""); // Leave empty if account name is unavailable
        }
        body.put("creditorAccount", creditorAccount);

        // Amount details
        Map<String, String> amount = new LinkedHashMap<>();
        String tag32B = block4.getTagValue("32B"); // Tag 32B: Currency and amount
        if (tag32B != null && tag32B.length() >= 3) {
            amount.put("currency", tag32B.substring(0, 3)); // Extract currency from first 3 characters
            amount.put("value", tag32B.substring(3).replace(",", ".")); // Extract amount, replacing comma with dot
        }
        body.put("amount", amount);

        // Construct final response
        response.put("header", header);
        response.put("body", body);

        return response;
    }
}
