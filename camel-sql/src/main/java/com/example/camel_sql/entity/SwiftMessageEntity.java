package com.example.camel_sql.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class SwiftMessageEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //block1
    private String applicationId;
    private String serviceId;
    private String logicalTerminal;
    private String sessionNumber;
    private String sequenceNumber;

    //block2
    private String messageType;
    private String receiverAddress;
    private String messagePriority;
    private String deliveryMonitoring;

    //block3
    private String messageUserReference;  // Field 108
    private String bankingPriority;      // Field 113
    private String validationFlag;       // Field 119
    private String additionalMessageInformation;  // Field 115
    private String serviceTypeIdentifier; // Field 111

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(String applicationId) {
        this.applicationId = applicationId;
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public String getLogicalTerminal() {
        return logicalTerminal;
    }

    public void setLogicalTerminal(String logicalTerminal) {
        this.logicalTerminal = logicalTerminal;
    }

    public String getSessionNumber() {
        return sessionNumber;
    }

    public void setSessionNumber(String sessionNumber) {
        this.sessionNumber = sessionNumber;
    }

    public String getSequenceNumber() {
        return sequenceNumber;
    }

    public void setSequenceNumber(String sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
    }

    public String getMessageType() {
        return messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    public String getReceiverAddress() {
        return receiverAddress;
    }

    public void setReceiverAddress(String receiverAddress) {
        this.receiverAddress = receiverAddress;
    }

    public String getMessagePriority() {
        return messagePriority;
    }

    public void setMessagePriority(String messagePriority) {
        this.messagePriority = messagePriority;
    }

    public String getDeliveryMonitoring() {
        return deliveryMonitoring;
    }

    public void setDeliveryMonitoring(String deliveryMonitoring) {
        this.deliveryMonitoring = deliveryMonitoring;
    }

    public String getMessageUserReference() {
        return messageUserReference;
    }

    public void setMessageUserReference(String messageUserReference) {
        this.messageUserReference = messageUserReference;
    }

    public String getBankingPriority() {
        return bankingPriority;
    }

    public void setBankingPriority(String bankingPriority) {
        this.bankingPriority = bankingPriority;
    }

    public String getValidationFlag() {
        return validationFlag;
    }

    public void setValidationFlag(String validationFlag) {
        this.validationFlag = validationFlag;
    }

    public String getAdditionalMessageInformation() {
        return additionalMessageInformation;
    }

    public void setAdditionalMessageInformation(String additionalMessageInformation) {
        this.additionalMessageInformation = additionalMessageInformation;
    }

    public String getServiceTypeIdentifier() {
        return serviceTypeIdentifier;
    }

    public void setServiceTypeIdentifier(String serviceTypeIdentifier) {
        this.serviceTypeIdentifier = serviceTypeIdentifier;
    }
}