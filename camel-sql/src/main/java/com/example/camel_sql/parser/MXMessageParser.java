package com.example.camel_sql.parser;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Component
public class MXMessageParser {

    public byte[] convertMXMessage(JsonNode jsonNode)  {

        StringBuilder mxMessageBuilder = new StringBuilder();

        mxMessageBuilder.append("<Doc:Document xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" ")
                .append("xmlns:Doc=\"urn:swift:xsd:acmt.001.001.02\">\n")
                .append("  <Doc:AcctOpngInstrV02>\n");

        mxMessageBuilder.append("    <Doc:MsgId>\n")
                .append("      <Doc:Id>").append(jsonNode.path("AcctOpngInstrV02").path("MsgId").path("Id").asText()).append("</Doc:Id>\n")
                .append("      <Doc:CreDtTm>").append(jsonNode.path("AcctOpngInstrV02").path("MsgId").path("CreDtTm").asText()).append("</Doc:CreDtTm>\n")
                .append("    </Doc:MsgId>\n");

        mxMessageBuilder.append("    <Doc:OrdrRef>\n")
                .append("      <Doc:OrdrRef>").append(jsonNode.path("AcctOpngInstrV02").path("OrdrRef").path("OrdrRef").asText()).append("</Doc:OrdrRef>\n")
                .append("      <Doc:MstrRef>").append(jsonNode.path("AcctOpngInstrV02").path("OrdrRef").path("MstrRef").asText()).append("</Doc:MstrRef>\n")
                .append("    </Doc:OrdrRef>\n");

        mxMessageBuilder.append("    <Doc:OthrRefs>\n")
                .append("      <Doc:PrvsRef>").append(jsonNode.path("AcctOpngInstrV02").path("OthrRefs").path("PrvsRef").asText()).append("</Doc:PrvsRef>\n")
                .append("      <Doc:RltdRef>").append(jsonNode.path("AcctOpngInstrV02").path("OthrRefs").path("RltdRef").asText()).append("</Doc:RltdRef>\n")
                .append("    </Doc:OthrRefs>\n");

        JsonNode initgPtyNode = jsonNode.path("AcctOpngInstrV02").path("InitgPty");
        mxMessageBuilder.append("    <Doc:InitgPty>\n")
                .append("      <Doc:Nm>").append(initgPtyNode.path("Nm").asText()).append("</Doc:Nm>\n")
                .append("      <Doc:Id>\n")
                .append("        <Doc:OrgId>\n")
                .append("          <Doc:BICFI>").append(initgPtyNode.path("Id").path("OrgId").path("BICFI").asText()).append("</Doc:BICFI>\n")
                .append("        </Doc:OrgId>\n")
                .append("      </Doc:Id>\n")
                .append("    </Doc:InitgPty>\n");

        JsonNode acctNode = jsonNode.path("AcctOpngInstrV02").path("Acct");
        mxMessageBuilder.append("    <Doc:Acct>\n")
                .append("      <Doc:Id>\n")
                .append("        <Doc:IBAN>").append(acctNode.path("Id").path("IBAN").asText()).append("</Doc:IBAN>\n")
                .append("      </Doc:Id>\n")
                .append("      <Doc:Svcr>\n")
                .append("        <Doc:FinInstnId>\n")
                .append("          <Doc:BICFI>").append(acctNode.path("Svcr").path("FinInstnId").path("BICFI").asText()).append("</Doc:BICFI>\n")
                .append("        </Doc:FinInstnId>\n")
                .append("      </Doc:Svcr>\n")
                .append("    </Doc:Acct>\n");

        JsonNode cshAcctNode = jsonNode.path("AcctOpngInstrV02").path("CshAcct");
        mxMessageBuilder.append("    <Doc:CshAcct>\n")
                .append("      <Doc:Id>\n")
                .append("        <Doc:IBAN>").append(cshAcctNode.path("Id").path("IBAN").asText()).append("</Doc:IBAN>\n")
                .append("      </Doc:Id>\n")
                .append("      <Doc:Svcr>\n")
                .append("        <Doc:FinInstnId>\n")
                .append("          <Doc:BICFI>").append(cshAcctNode.path("Svcr").path("FinInstnId").path("BICFI").asText()).append("</Doc:BICFI>\n")
                .append("        </Doc:FinInstnId>\n")
                .append("      </Doc:Svcr>\n")
                .append("    </Doc:CshAcct>\n");

        JsonNode trxDtlsNode = jsonNode.path("AcctOpngInstrV02").path("TrxDtls");
        mxMessageBuilder.append("    <Doc:TrxDtls>\n")
                .append("      <Doc:Amt>\n")
                .append("        <Doc:InstdAmt Ccy=\"")
                .append(trxDtlsNode.path("Amt").path("InstdAmt").path("Ccy").asText()).append("\">")
                .append(trxDtlsNode.path("Amt").path("InstdAmt").asText()).append("</Doc:InstdAmt>\n")
                .append("      </Doc:Amt>\n")
                .append("      <Doc:Purp>\n")
                .append("        <Doc:Cd>").append(trxDtlsNode.path("Purp").path("Cd").asText()).append("</Doc:Cd>\n")
                .append("      </Doc:Purp>\n")
                .append("    </Doc:TrxDtls>\n");

        JsonNode sgntrNode = jsonNode.path("AcctOpngInstrV02").path("Sgntr");
        mxMessageBuilder.append("    <Doc:Sgntr>\n")
                .append("      <Doc:Nm>").append(sgntrNode.path("Nm").asText()).append("</Doc:Nm>\n")
                .append("      <Doc:Date>").append(sgntrNode.path("Date").asText()).append("</Doc:Date>\n")
                .append("    </Doc:Sgntr>\n");

        mxMessageBuilder.append("  </Doc:AcctOpngInstrV02>\n")
                .append("</Doc:Document>\n");

        // Convert the message to byte array
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        try {
            outputStream.write(mxMessageBuilder.toString().getBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return outputStream.toByteArray();
    }
}

