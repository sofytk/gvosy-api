package com.sonchasapps.dto;

import java.util.UUID;

public class KafkaAudioRequest {

    private String messageId;
    private UUID userId;
    private UUID assistanceId;
    private String audioUrl;

    public KafkaAudioRequest(String messageId, UUID userId, UUID assistanceId, String audioUrl) {
        this.messageId = messageId;
        this.userId = userId;
        this.assistanceId = assistanceId;
        this.audioUrl = audioUrl;
    }

    public KafkaAudioRequest() {
    }

    public String getMessageId() {
        return messageId;
    }

    public UUID getAssistanceId() {
        return assistanceId;
    }

    public void setAssistanceId(UUID assistanceId) {
        this.assistanceId = assistanceId;
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

    public String getAudioUrl() {
        return audioUrl;
    }

    public void setAudioUrl(String audioUrl) {
        this.audioUrl = audioUrl;
    }
}

