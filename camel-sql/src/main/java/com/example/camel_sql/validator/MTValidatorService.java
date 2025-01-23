package com.example.camel_sql.validator;

import com.prowidesoftware.swift.model.SwiftMessage;
import com.prowidesoftware.swift.model.Tag;
import com.prowidesoftware.swift.model.mt.AbstractMT;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class MTValidatorService {


    public void validateMTFile(SwiftMessage swiftMessage, StringBuilder errorMessages) throws IOException {
        if (swiftMessage == null) {
            errorMessages.append("MtSwiftMessage or its SwiftMessage is missing. ");
            return;
        }

        if (swiftMessage.getBlock4() == null) {
            errorMessages.append("Block4 is missing in SwiftMessage. ");
            return;
        }

        String messageType = swiftMessage.getType();
        if (messageType == null) {
            errorMessages.append("Message type is missing in Swift message. ");
            return;
        }

        AbstractMT abstractMT = swiftMessage.toMT();
        switch (messageType) {
            case "300":
                validateMT300(abstractMT, errorMessages);
                break;
            case "305":
                validateMT305(abstractMT, errorMessages);
                break;
            default:
                errorMessages.append("Unsupported message type: ").append(messageType).append(". ");
        }
    }



    private void validateField15A(AbstractMT mt, StringBuilder errorMessages) {

        SwiftMessage swiftMessage = mt.getSwiftMessage();

        // Check if SwiftMessage and Block4 are present
        if (swiftMessage == null || swiftMessage.getBlock4() == null) {
            errorMessages.append("Swift message or Block4 is missing. ");
            return;
        }

        // Fetch the field 15A from Block4
        Tag field15A = swiftMessage.getBlock4().getTagByName("15A");

        if(field15A.getName().isEmpty()) {
            errorMessages.append("Mandatory field 15A is missing. ");

        }
    }

    private void validateMT305(AbstractMT mt, StringBuilder errorMessages) {

        SwiftMessage swiftMessage = mt.getSwiftMessage();
        Tag field15A = swiftMessage.getBlock4().getTagByName("15A");

        if (!field15A.getName().isEmpty())
        {
            validateField20(mt, errorMessages);
            validateField21(mt, errorMessages);
            validateField22(mt, errorMessages);
            validateField23(mt, errorMessages);
            validateField82A(mt, errorMessages);
            validateField87A(mt, errorMessages);
            validateField30(mt, errorMessages);
            validateField31G(mt, errorMessages);
            validateField26F(mt, errorMessages);
            validateField32B(mt, errorMessages);
            validateField36(mt , errorMessages);
            validateField33B(mt, errorMessages);
            validateField37K(mt, errorMessages);
            validateField34P(mt, errorMessages);
//            validateField34R(mt, errorMessages);
            validateField57A(mt, errorMessages);
        }else {
            errorMessages.append("Mandatory field 15A is missing. ");
        }

    }



    private void validateMT300(AbstractMT mt, StringBuilder errorMessages) {

        SwiftMessage swiftMessage = mt.getSwiftMessage();
        Tag field15A = swiftMessage.getBlock4().getTagByName("15A");

        // Validating Sequence A
        if (!field15A.getName().isEmpty())
        {
            //Validating the Mandatory Fields
            validateField20(mt , errorMessages);
            validateField22A(mt , errorMessages);
            validateField22C(mt , errorMessages);
            validateField82A(mt , errorMessages);
            validateField87A(mt , errorMessages);


            //Validating Optional Fields

        }else {
            errorMessages.append("Mandatory field 15A is missing. ");
        }

        Tag field15B = swiftMessage.getBlock4().getTagByName("15B");

        //Sequence B
        if (!field15B.getName().isEmpty())
        {

        }




    }












    private void validateField20(AbstractMT mt300, StringBuilder errorMessages) {
        SwiftMessage swiftMessage = mt300.getSwiftMessage();

        // Check if SwiftMessage and Block4 are present
        if (swiftMessage == null || swiftMessage.getBlock4() == null) {
            errorMessages.append("Swift message or Block4 is missing. ");
            return;
        }

        // Fetch the field 20 from Block4
        Tag field20 = swiftMessage.getBlock4().getTagByName("20");

        // Validate the field
        if (field20 == null || field20.getValue() == null || field20.getValue().isEmpty()) {
            errorMessages.append("Mandatory field 20 is missing. ");
        } else {
            String field20Value = field20.getValue();

            // Check if value is alphanumeric and within 16 characters
            if (!field20Value.matches("^[a-zA-Z0-9]{1,16}$")) {
                errorMessages.append("Field 20 must be alphanumeric and up to 16 characters. ");
            }
        }
    }


    private void validateField22A(AbstractMT mt300, StringBuilder errorMessages) {
        SwiftMessage swiftMessage = mt300.getSwiftMessage();

        // Check if SwiftMessage and Block4 are present
        if (swiftMessage == null || swiftMessage.getBlock4() == null) {
            errorMessages.append("Swift message or Block4 is missing. ");
            return;
        }

        // Fetch the field 22A from Block4
        Tag field22A = swiftMessage.getBlock4().getTagByName("22A");

        // Validate the field
        if (field22A == null || field22A.getValue() == null || field22A.getValue().isEmpty()) {
            errorMessages.append("Mandatory field 22A is missing. ");
        } else {
            String field22AValue = field22A.getValue();

            // Validate the structure and allowed values
            String regex = "^(NEWT|CANC|AMND|EXOP)$";
            if (!field22AValue.matches(regex)) {
                errorMessages.append("Field 22A must be one of the allowed values: NEWT, CANC, AMND, EXOP. ");
            }
        }
    }


    private void validateField22C(AbstractMT mt300, StringBuilder errorMessages) {
        SwiftMessage swiftMessage = mt300.getSwiftMessage();

        // Check if SwiftMessage and Block4 are present
        if (swiftMessage == null || swiftMessage.getBlock4() == null) {
            errorMessages.append("Swift message or Block4 is missing. ");
            return;
        }

        // Fetch the field 22C from Block4
        Tag field22C = swiftMessage.getBlock4().getTagByName("22C");

        // Validate the field
        if (field22C == null || field22C.getValue() == null || field22C.getValue().isEmpty()) {
            errorMessages.append("Mandatory field 22C is missing. ");
            return;
        }

        String value = field22C.getValue();

        // Validate format 4!a2!c4!n4!a2!c
        if (!value.matches("^[A-Z]{4}[A-Z0-9]{2}\\d{4}[A-Z]{4}[A-Z0-9]{2}$")) {
            errorMessages.append("Field 22C does not match the expected format (4!a2!c4!n4!a2!c). ");
            return;
        }

        // Extract components
        String senderBankCode = value.substring(0, 4);
        String intermediaryCode = value.substring(4, 6);
        String numericCode = value.substring(6, 10);
        String receiverBankCode = value.substring(10, 14);
        String locationCode = value.substring(14, 16);


        // Validate that the numeric code represents the last 4 non-zero digits of the exchange rate
        try {
            int numericValue = Integer.parseInt(numericCode);
            if (numericValue == 0) {
                errorMessages.append("Numeric portion of Field 22C (exchange rate) cannot be zero. ");
            }
        } catch (NumberFormatException e) {
            errorMessages.append("Numeric portion of Field 22C (exchange rate) must be a valid integer. ");
        }
    }


    private void validateField82A(AbstractMT mt300, StringBuilder errorMessages) {
        SwiftMessage swiftMessage = mt300.getSwiftMessage();

        // Check if SwiftMessage and Block4 are present
        if (swiftMessage == null || swiftMessage.getBlock4() == null) {
            errorMessages.append("Swift message or Block4 is missing. ");
            return;
        }

        // Fetch the field 82A from Block4
        Tag field82A = swiftMessage.getBlock4().getTagByName("82A");

        // Validate the field
        if (field82A == null || field82A.getValue() == null || field82A.getValue().isEmpty()) {
            errorMessages.append("Mandatory field 82A is missing. ");
        } else {
            String field82AValue = field82A.getValue();

            // Validate the structure of Field 82A (BIC format)
            String regex = "^[A-Z]{4}[A-Z]{2}[A-Z0-9]{2}([A-Z0-9]{3})?$";
            if (!field82AValue.matches(regex)) {
                errorMessages.append("Field 82A must follow the BIC format (4!a2!a2!c[3!c]). ");
            }
        }
    }


    private void validateField87A(AbstractMT mt300, StringBuilder errorMessages) {
        SwiftMessage swiftMessage = mt300.getSwiftMessage();

        // Check if SwiftMessage and Block4 are present
        if (swiftMessage == null || swiftMessage.getBlock4() == null) {
            errorMessages.append("Swift message or Block4 is missing. ");
            return;
        }

        // Fetch the field 87A from Block4
        Tag field87A = swiftMessage.getBlock4().getTagByName("87A");

        // Validate the field
        if (field87A == null || field87A.getValue() == null || field87A.getValue().isEmpty()) {
            errorMessages.append("Mandatory field 87A is missing. ");
        } else {
            String field87AValue = field87A.getValue();

            // Validate the structure of Field 87A
            String regex = "^(/.{1})?(\\/[a-zA-Z0-9]{1,34})?[A-Z]{4}[A-Z]{2}[A-Z0-9]{2}([A-Z0-9]{3})?$";
            if (!field87AValue.matches(regex)) {
                errorMessages.append("Field 87A must follow the format [/1!a][/34x] 4!a2!a2!c[3!c]. ");
            }
        }
    }

    private void validateField30T(AbstractMT mt300, StringBuilder errorMessages) {
        SwiftMessage swiftMessage = mt300.getSwiftMessage();

        // Check if SwiftMessage and Block4 are present
        if (swiftMessage == null || swiftMessage.getBlock4() == null) {
            errorMessages.append("Swift message or Block4 is missing. ");
            return;
        }

        // Fetch the field 30T from Block4
        Tag field30T = swiftMessage.getBlock4().getTagByName("30T");

        // Validate the field
        if (field30T == null || field30T.getValue() == null || field30T.getValue().isEmpty()) {
            errorMessages.append("Mandatory field 30T is missing. ");
        }
    }

    private void validateField30V(AbstractMT mt300, StringBuilder errorMessages) {
        SwiftMessage swiftMessage = mt300.getSwiftMessage();

        // Check if SwiftMessage and Block4 are present
        if (swiftMessage == null || swiftMessage.getBlock4() == null) {
            errorMessages.append("Swift message or Block4 is missing. ");
            return;
        }

        // Fetch the field 30V from Block4
        Tag field30V = swiftMessage.getBlock4().getTagByName("30V");

        // Validate the field
        if (field30V == null || field30V.getValue() == null || field30V.getValue().isEmpty()) {
            errorMessages.append("Mandatory field 30V is missing. ");
        }
    }

    private void validateField36(AbstractMT mt300, StringBuilder errorMessages) {
        SwiftMessage swiftMessage = mt300.getSwiftMessage();

        // Check if SwiftMessage and Block4 are present
        if (swiftMessage == null || swiftMessage.getBlock4() == null) {
            errorMessages.append("Swift message or Block4 is missing. ");
            return;
        }

        // Fetch the field 36 from Block4
        Tag field36 = swiftMessage.getBlock4().getTagByName("36");

        // Validate the field
        if (field36 == null || field36.getValue() == null || field36.getValue().isEmpty()) {
            errorMessages.append("Mandatory field 36 is missing. ");
        }
    }

    private void validateField32B(AbstractMT mt300, StringBuilder errorMessages) {
        SwiftMessage swiftMessage = mt300.getSwiftMessage();

        // Check if SwiftMessage and Block4 are present
        if (swiftMessage == null || swiftMessage.getBlock4() == null) {
            errorMessages.append("Swift message or Block4 is missing. ");
            return;
        }

        // Fetch the field 32B from Block4
        Tag field32B = swiftMessage.getBlock4().getTagByName("32B");

        // Validate the field
        if (field32B == null || field32B.getValue() == null || field32B.getValue().isEmpty()) {
            errorMessages.append("Mandatory field 32B is missing. ");
        }
    }

    private void validateField33B(AbstractMT mt300, StringBuilder errorMessages) {
        SwiftMessage swiftMessage = mt300.getSwiftMessage();

        // Check if SwiftMessage and Block4 are present
        if (swiftMessage == null || swiftMessage.getBlock4() == null) {
            errorMessages.append("Swift message or Block4 is missing. ");
            return;
        }

        // Fetch the field 33B from Block4
        Tag field33B = swiftMessage.getBlock4().getTagByName("33B");

        // Validate the field
        if (field33B == null || field33B.getValue() == null || field33B.getValue().isEmpty()) {
            errorMessages.append("Mandatory field 33B is missing. ");
        }
    }

    private void validateField57A(AbstractMT mt300, StringBuilder errorMessages) {
        SwiftMessage swiftMessage = mt300.getSwiftMessage();

        // Check if SwiftMessage and Block4 are present
        if (swiftMessage == null || swiftMessage.getBlock4() == null) {
            errorMessages.append("Swift message or Block4 is missing. ");
            return;
        }

        // Fetch the field 57A from Block4
        Tag field57A = swiftMessage.getBlock4().getTagByName("57A");

        // Validate the field
        if (field57A == null || field57A.getValue() == null || field57A.getValue().isEmpty()) {
            errorMessages.append("Mandatory field 57A is missing. ");
        }
    }

    private void validateField34R(AbstractMT mt, StringBuilder errorMessages) {
        SwiftMessage swiftMessage = mt.getSwiftMessage();

        if (swiftMessage == null || swiftMessage.getBlock4() == null) {
            errorMessages.append("Swift message or Block4 is missing. ");
            return;
        }

        Tag field34R = swiftMessage.getBlock4().getTagByName("34R");

        if (field34R == null || field34R.getValue() == null || field34R.getValue().isEmpty()) {
            errorMessages.append("Mandatory field 34R is missing. ");
        }
    }

    private void validateField34P(AbstractMT mt, StringBuilder errorMessages) {
        SwiftMessage swiftMessage = mt.getSwiftMessage();

        if (swiftMessage == null || swiftMessage.getBlock4() == null) {
            errorMessages.append("Swift message or Block4 is missing. ");
            return;
        }

        Tag field34P = swiftMessage.getBlock4().getTagByName("34P");

        if (field34P == null || field34P.getValue() == null || field34P.getValue().isEmpty()) {
            errorMessages.append("Mandatory field 34P is missing. ");
        }
    }

    private void validateField37K(AbstractMT mt, StringBuilder errorMessages) {
        SwiftMessage swiftMessage = mt.getSwiftMessage();

        if (swiftMessage == null || swiftMessage.getBlock4() == null) {
            errorMessages.append("Swift message or Block4 is missing. ");
            return;
        }

        Tag field37K = swiftMessage.getBlock4().getTagByName("37K");

        if (field37K == null || field37K.getValue() == null || field37K.getValue().isEmpty()) {
            errorMessages.append("Mandatory field 37K is missing. ");
        }
    }

    private void validateField26F(AbstractMT mt, StringBuilder errorMessages) {
        SwiftMessage swiftMessage = mt.getSwiftMessage();

        if (swiftMessage == null || swiftMessage.getBlock4() == null) {
            errorMessages.append("Swift message or Block4 is missing. ");
            return;
        }

        Tag field26F = swiftMessage.getBlock4().getTagByName("26F");

        if (field26F == null || field26F.getValue() == null || field26F.getValue().isEmpty()) {
            errorMessages.append("Mandatory field 26F is missing. ");
        }
    }

    private void validateField31G(AbstractMT mt, StringBuilder errorMessages) {
        SwiftMessage swiftMessage = mt.getSwiftMessage();

        if (swiftMessage == null || swiftMessage.getBlock4() == null) {
            errorMessages.append("Swift message or Block4 is missing. ");
            return;
        }

        Tag field31G = swiftMessage.getBlock4().getTagByName("31G");

        if (field31G == null || field31G.getValue() == null || field31G.getValue().isEmpty()) {
            errorMessages.append("Mandatory field 31G is missing. ");
        }
    }

    private void validateField30(AbstractMT mt, StringBuilder errorMessages) {
        SwiftMessage swiftMessage = mt.getSwiftMessage();

        if (swiftMessage == null || swiftMessage.getBlock4() == null) {
            errorMessages.append("Swift message or Block4 is missing. ");
            return;
        }

        Tag field30 = swiftMessage.getBlock4().getTagByName("30");

        if (field30 == null || field30.getValue() == null || field30.getValue().isEmpty()) {
            errorMessages.append("Mandatory field 30 is missing. ");
        }
    }

    private void validateField23(AbstractMT mt, StringBuilder errorMessages) {
        SwiftMessage swiftMessage = mt.getSwiftMessage();

        if (swiftMessage == null || swiftMessage.getBlock4() == null) {
            errorMessages.append("Swift message or Block4 is missing. ");
            return;
        }

        Tag field23 = swiftMessage.getBlock4().getTagByName("23");

        if (field23 == null || field23.getValue() == null || field23.getValue().isEmpty()) {
            errorMessages.append("Mandatory field 23 is missing. ");
        }
    }

    private void validateField22(AbstractMT mt, StringBuilder errorMessages) {
        SwiftMessage swiftMessage = mt.getSwiftMessage();

        if (swiftMessage == null || swiftMessage.getBlock4() == null) {
            errorMessages.append("Swift message or Block4 is missing. ");
            return;
        }

        Tag field22 = swiftMessage.getBlock4().getTagByName("22");

        if (field22 == null || field22.getValue() == null || field22.getValue().isEmpty()) {
            errorMessages.append("Mandatory field 22 is missing. ");
        }
    }

    private void validateField21(AbstractMT mt, StringBuilder errorMessages) {
        SwiftMessage swiftMessage = mt.getSwiftMessage();

        if (swiftMessage == null || swiftMessage.getBlock4() == null) {
            errorMessages.append("Swift message or Block4 is missing. ");
            return;
        }

        Tag field21 = swiftMessage.getBlock4().getTagByName("21");

        if (field21 == null || field21.getValue() == null || field21.getValue().isEmpty()) {
            errorMessages.append("Mandatory field 21 is missing. ");
        }
    }


}
