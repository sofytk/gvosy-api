package com.sonchasapps.models;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.FieldType;
import org.springframework.data.mongodb.core.mapping.MongoId;
import java.time.Instant;
import java.util.UUID;

@Document("messages")
public class MessageEntity {

    @MongoId(FieldType.OBJECT_ID)
    private String id;

    private UUID userId;
    private UUID assistantId;
    private String type;
    private String text;
    private String audioUrl;
    private Instant createdAt;

    public MessageEntity() {}

    public MessageEntity(UUID assistantId, UUID userId, String type, String audioUrl, Instant createdAt) {
        this.assistantId = assistantId;
        this.userId = userId;
        this.type = type;
        this.audioUrl = audioUrl;
        this.createdAt = createdAt;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getAudioUrl() {
        return audioUrl;
    }

    public void setAudioUrl(String audioUrl) {
        this.audioUrl = audioUrl;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public UUID getAssistantId() {
        return assistantId;
    }

    public void setAssistantId(UUID assistantId) {
        this.assistantId = assistantId;
    }
}
