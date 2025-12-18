package com.sonchasapps.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;



public class KafkaAudioRequest {

    private String messageId;
    private UUID userId;
    String assistantDescription;
    int assistantAge;
    boolean assistantSex;
    private String audioUrl;

    public KafkaAudioRequest(String messageId, String assistantDescription, UUID userId, int assistantAge, boolean assistantSex, String audioUrl) {
        this.messageId = messageId;
        this.assistantDescription = assistantDescription;
        this.userId = userId;
        this.assistantAge = assistantAge;
        this.assistantSex = assistantSex;
        this.audioUrl = audioUrl;
    }

    public KafkaAudioRequest() {
    }

    public String getMessageId() {
        return messageId;
    }

    public String getAssistantDescription() {
        return assistantDescription;
    }

    public void setAssistantDescription(String assistantDescription) {
        this.assistantDescription = assistantDescription;
    }

    public int getAssistantAge() {
        return assistantAge;
    }

    public void setAssistantAge(int assistantAge) {
        this.assistantAge = assistantAge;
    }

    public boolean getAssistantSex() {
        return assistantSex;
    }

    public void setAssistantSex(boolean assistantSex) {
        this.assistantSex = assistantSex;
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

