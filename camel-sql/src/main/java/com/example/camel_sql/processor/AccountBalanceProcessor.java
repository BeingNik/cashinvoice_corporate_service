package com.example.camel_sql.processor;


import com.example.camel_sql.dto.AccountBalanceResponseDto;
import com.example.camel_sql.entity.AccountBalanceEntity;
import com.example.camel_sql.entity.Response;
import com.example.camel_sql.repository.AccountBalanceRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class AccountBalanceProcessor {

    Logger logger = LoggerFactory.getLogger(AccountBalanceProcessor.class);

    @Autowired
    private AccountBalanceRepository accountBalanceRepository;

    public Response processAccountData(Object exchangeBody, LocalDateTime startTime, LocalDateTime endTime) {
        List<AccountBalanceResponseDto> responseDtos = new ArrayList<>();

        if (exchangeBody instanceof List) {
            List<Map<String, Object>> accountDataList = (List<Map<String, Object>>) exchangeBody;

            for (Map<String, Object> accountData : accountDataList) {
                String accountIdentification = (String) accountData.get("accountIdentification");
                double balance = extractBalance(accountData);

                AccountBalanceEntity accountBalanceEntity = accountBalanceRepository
                        .findByAccountIdentification(accountIdentification)
                        .orElse(new AccountBalanceEntity());

                accountBalanceEntity.setAccountIdentification(accountIdentification);
                accountBalanceEntity.setBalance(balance);
                accountBalanceEntity.setStartTime(startTime);
                accountBalanceEntity.setEndTime(endTime);
                accountBalanceRepository.save(accountBalanceEntity);

                AccountBalanceResponseDto dto = createResponseDto(accountData, balance);
                responseDtos.add(dto);
            }
        }

        return responseDtos.isEmpty() ?
                Response.success("No balances found", responseDtos) :
                Response.success("Balances processed successfully", responseDtos);
    }

    private double extractBalance(Map<String, Object> accountData) {
        Map<String, Object> balances = (Map<String, Object>) accountData.get("balances");
        Map<String, Object> currentBalance = (Map<String, Object>) balances.get("currentBalance");
        Map<String, Object> balanceAmount = (Map<String, Object>) currentBalance.get("balanceAmount");
        Map<String, Object> amountDetails = (Map<String, Object>) balanceAmount.get("amount");
        return Double.parseDouble((String) amountDetails.get("amount"));
    }

    private AccountBalanceResponseDto createResponseDto(Map<String, Object> accountData, double balance) {
        AccountBalanceResponseDto dto = new AccountBalanceResponseDto();
        dto.setAccountIdentification((String) accountData.get("accountIdentification"));
        dto.setAlternateIdentification((String) accountData.get("alternateIdentification"));
        dto.setName((String) accountData.get("name"));
        dto.setOwnerName((String) accountData.get("ownerName"));
        dto.setCurrency((String) accountData.get("currency"));
        dto.setRelatedAccount((String) accountData.get("relatedAccount"));
        dto.setBalances((Map<String, Object>) accountData.get("balances"));
        dto.setStatus((String) accountData.get("status"));
        dto.setCreditLimit((Map<String, Object>) accountData.get("creditLimit"));
        return dto;
    }
}

