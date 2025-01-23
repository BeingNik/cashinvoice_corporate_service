package com.example.camel_sql.parser;

import com.fasterxml.jackson.databind.JsonNode;
import com.prowidesoftware.swift.model.field.*;
import com.prowidesoftware.swift.model.mt.mt1xx.MT103;
import com.prowidesoftware.swift.model.mt.mt7xx.MT760;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class MTMessageParser {

    public byte[] convertMTMessage(JsonNode jsonNode)  {
        JsonNode partyNode = jsonNode.path("party").get(0);
        String messageType = partyNode.path("identification").asText();

        Pattern pattern = Pattern.compile("\\d+$");
        Matcher matcher = pattern.matcher(messageType);

        if (matcher.find()) {
            String numberPart = matcher.group();
            switch (numberPart) {
                case "1234":
                    try {
                        return processMT103(jsonNode);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                case "760":
                    try {
                        return processMT760(jsonNode);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                default:
                    throw new IllegalArgumentException("Unsupported number type: " + numberPart);
            }
        } else {
            throw new IllegalArgumentException("No number found in the identification string: " + messageType);
        }

    }
    private byte[] processMT103(JsonNode jsonNode) throws Exception {
        MT103 mt103 = new MT103();

        mt103.setSender(jsonNode.path("account_with_bank").path("any_bic").asText());
        mt103.setReceiver(jsonNode.path("party").path(0).path("any_bic").asText());
        mt103.addField(new Field20(jsonNode.path("applicant_issuance_request_identification").asText()));
        mt103.addField(new Field30(jsonNode.path("application_date").asText()));

        JsonNode undertakingAmountNode = jsonNode.path("undertaking_amount");
        mt103.addField(new Field32B(undertakingAmountNode.path("currency").asText() + " " + undertakingAmountNode.path("amount").asText()));

        if (!jsonNode.path("presentation_instructions").asText().isEmpty()) {
            mt103.addField(new Field78(jsonNode.path("presentation_instructions").asText()));
        }

        JsonNode partyNode = jsonNode.path("party").get(0);
        mt103.addField(new Field50K("/" + partyNode.path("identification").asText() + "\n" +
                partyNode.path("name").asText() + "\n" +
                partyNode.path("address_line").get(0).asText() + "\n" +
                partyNode.path("country").asText()));

        mt103.addField(new Field59("/" + jsonNode.path("account_with_bank").path("any_bic").asText() + "\n" +
                jsonNode.path("deliver_to_name").asText() + "\n" +
                jsonNode.path("deliver_to_address").get(0).asText() + "\n" +
                jsonNode.path("deliver_to_country").asText()));

        if (!jsonNode.path("presentation_medium").asText().isEmpty()) {
            mt103.addField(new Field70(jsonNode.path("presentation_medium").asText()));
        }

        if (jsonNode.has("charge")) {
            JsonNode chargeNode = jsonNode.path("charge").get(0);
            mt103.addField(new Field71A(chargeNode.path("charge_type").asText() + " " +
                    chargeNode.path("charge_amount").path("currency").asText() + " " +
                    chargeNode.path("charge_amount").path("amount").asText()));
        }

        // Convert MT103 message to byte array
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(mt103.message().getBytes());
        return outputStream.toByteArray();  // Return byte array
    }

    private byte[] processMT760(JsonNode jsonNode) throws Exception {
        MT760 mt760 = new MT760();
        mt760.setSender("BANKUS33XXX");
        mt760.setReceiver("BANKDEFFXXX");
        mt760.addField(new Field20("XYZ999-123"));
        mt760.addField(new Field32B("EUR 30000"));

        // Convert MT760 message to byte array
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(mt760.message().getBytes());
        return outputStream.toByteArray();  // Return byte array
    }
}
