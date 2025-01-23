package com.example.camel_sql.service.impl;


import com.example.camel_sql.entity.Response;
import com.example.camel_sql.processor.AccountBalanceProcessor;
import com.example.camel_sql.service.AccountBalanceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
public class AccountBalanceServiceImpl implements AccountBalanceService {

    private static final Logger logger = LoggerFactory.getLogger(AccountBalanceServiceImpl.class);

    @Value("${standalone.token.url}")
    private String tokenUrl;

    @Value("${standalone.check.balance.url}")
    private String checkBalanceBaseUrl;

    @Autowired
    private AccountBalanceProcessor accountBalanceProcessor;

    private final RestTemplate restTemplate;

    public AccountBalanceServiceImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public Response getBalances(String accountIdentification) {
        try {
            String token = generateToken();
            if (token == null || token.isEmpty()) {
                return Response.fail("Failed to generate token", "TOKEN_ERROR", "Token generation failed");
            }

            String checkBalancesUrl = buildCheckBalanceUrl(accountIdentification, token);
            Response response = restTemplate.getForObject(checkBalancesUrl, Response.class);

            if (response == null || !"success".equals(response.getStatus())) {
                String errorMessage = (response != null) ? response.getMessage() : "Unknown error occurred";
                return Response.fail("Error fetching account balances", "FETCH_ERROR", errorMessage);
            }

            List<Map<String, Object>> accountDataList = null;

            if (response.getData() != null && response.getData() instanceof List) {
                accountDataList = (List<Map<String, Object>>) response.getData();
            } else {
                System.out.println("Unexpected data type. Expected List but got: " + response.getData().getClass());
            }

            if (accountDataList == null || accountDataList.isEmpty()) {
                return Response.success("No account found", List.of());
            }

            LocalDateTime startTime = LocalDateTime.now();
            LocalDateTime endTime = LocalDateTime.now();
            Response balanceDtos = accountBalanceProcessor.processAccountData(accountDataList, startTime, endTime);

            return Response.success("Bank accounts retrieved successfully.", balanceDtos.getData());
        } catch (Exception e) {
            return Response.fail("Failed to process balance check", "PROCESS_ERROR", e.getMessage());
        }
    }


    private String generateToken() {
        return restTemplate.getForObject(tokenUrl, String.class);
    }

    private String buildCheckBalanceUrl(String accountIdentification, String token) {
        return String.format(
                "%s?accountIdentification=%s&token=%s",
                checkBalanceBaseUrl, accountIdentification, token);
    }

}