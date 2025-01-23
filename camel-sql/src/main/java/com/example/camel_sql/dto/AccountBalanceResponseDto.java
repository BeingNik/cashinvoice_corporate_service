package com.example.camel_sql.dto;

import java.util.Map;

public class AccountBalanceResponseDto {
    private String accountIdentification;
    private String alternateIdentification;
    private String name;
    private String ownerName;
    private String currency;
    private String relatedAccount;
    private Map<String, Object> balances;
    private String status;
    private Map<String, Object> creditLimit;

    public String getAccountIdentification() {
        return accountIdentification;
    }

    public void setAccountIdentification(String accountIdentification) {
        this.accountIdentification = accountIdentification;
    }

    public String getAlternateIdentification() {
        return alternateIdentification;
    }

    public void setAlternateIdentification(String alternateIdentification) {
        this.alternateIdentification = alternateIdentification;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getRelatedAccount() {
        return relatedAccount;
    }

    public void setRelatedAccount(String relatedAccount) {
        this.relatedAccount = relatedAccount;
    }

    public Map<String, Object> getBalances() {
        return balances;
    }

    public void setBalances(Map<String, Object> balances) {
        this.balances = balances;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Map<String, Object> getCreditLimit() {
        return creditLimit;
    }

    public void setCreditLimit(Map<String, Object> creditLimit) {
        this.creditLimit = creditLimit;
    }
}
