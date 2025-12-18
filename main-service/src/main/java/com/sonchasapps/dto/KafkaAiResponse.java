package com.sonchasapps.dto;

import lombok.Data;

import java.util.Map;
import java.util.UUID;


public class KafkaAiResponse {
    private String messageId;
    private UUID userId;
    private String originalText;
    private String summary;

    public KafkaAiResponse(UUID userId, String messageId, String originalText, String summary) {
        this.userId = userId;
        this.messageId = messageId;
        this.originalText = originalText;
        this.summary = summary;
    }
    public KafkaAiResponse(){}

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public String getOriginalText() {
        return originalText;
    }

    public void setOriginalText(String originalText) {
        this.originalText = originalText;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }
}

