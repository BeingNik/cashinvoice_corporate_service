package com.example.camel_sql.entity;


import jakarta.persistence.*;

@Entity
@Table(name = "route_configurations")
public class RouteConfig {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String sourceChannelType;
    private String sourceEndpointType;
    private String sourcePath;
    private String sourceUrl;
    private String sourcePort;
    private String sourceUsername;
    private String sourcePassword;
    private String sourceHost;
    private String sourceMsgType;

    private String destinationPath;
    private String destinationChannelType;
    private String destinationEndpointType;
    private String destinationUrl;
    private String destinationPort;
    private String destinationUsername;
    private String destinationPassword;
    private String destinationHost;
    private String destinationMsgType;

    // Getters and Setters

    public String getDestinationMsgType() {
        return destinationMsgType;
    }

    public void setDestinationMsgType(String destinationMsgType) {
        this.destinationMsgType = destinationMsgType;
    }

    public String getSourceMsgType() {
        return sourceMsgType;
    }

    public void setSourceMsgType(String sourceMsgType) {
        this.sourceMsgType = sourceMsgType;
    }

    public String getSourceEndpointType() {
        return sourceEndpointType;
    }

    public void setSourceEndpointType(String sourceEndpointType) {
        this.sourceEndpointType = sourceEndpointType;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSourceChannelType() {
        return sourceChannelType;
    }

    public void setSourceChannelType(String sourceChannelType) {
        this.sourceChannelType = sourceChannelType;
    }

    public String getSourcePath() {
        return sourcePath;
    }

    public void setSourcePath(String sourcePath) {
        this.sourcePath = sourcePath;
    }

    public String getDestinationPath() {
        return destinationPath;
    }

    public void setDestinationPath(String destinationPath) {
        this.destinationPath = destinationPath;
    }

    public String getSourceUrl() {
        return sourceUrl;
    }

    public void setSourceUrl(String sourceUrl) {
        this.sourceUrl = sourceUrl;
    }

    public String getSourcePort() {
        return sourcePort;
    }

    public void setSourcePort(String sourcePort) {
        this.sourcePort = sourcePort;
    }

    public String getSourceUsername() {
        return sourceUsername;
    }

    public void setSourceUsername(String sourceUsername) {
        this.sourceUsername = sourceUsername;
    }

    public String getPassword() {
        return sourcePassword;
    }

    public void setPassword(String password) {
        this.sourcePassword = password;
    }

    public String getSourceHost() {
        return sourceHost;
    }

    public void setSourceHost(String sourceHost) {
        this.sourceHost = sourceHost;
    }

    public String getSourcePassword() {
        return sourcePassword;
    }

    public void setSourcePassword(String sourcePassword) {
        this.sourcePassword = sourcePassword;
    }

    public String getDestinationChannelType() {
        return destinationChannelType;
    }

    public void setDestinationChannelType(String destinationChannelType) {
        this.destinationChannelType = destinationChannelType;
    }

    public String getDestinationEndpointType() {
        return destinationEndpointType;
    }

    public void setDestinationEndpointType(String destinationEndpointType) {
        this.destinationEndpointType = destinationEndpointType;
    }

    public String getDestinationUrl() {
        return destinationUrl;
    }

    public void setDestinationUrl(String destinationUrl) {
        this.destinationUrl = destinationUrl;
    }

    public String getDestinationPort() {
        return destinationPort;
    }

    public void setDestinationPort(String destinationPort) {
        this.destinationPort = destinationPort;
    }

    public String getDestinationUsername() {
        return destinationUsername;
    }

    public void setDestinationUsername(String destinationUsername) {
        this.destinationUsername = destinationUsername;
    }

    public String getDestinationPassword() {
        return destinationPassword;
    }

    public void setDestinationPassword(String destinationPassword) {
        this.destinationPassword = destinationPassword;
    }

    public String getDestinationHost() {
        return destinationHost;
    }

    public void setDestinationHost(String destinationHost) {
        this.destinationHost = destinationHost;
    }
}
