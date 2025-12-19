package com.sonchasapps.models.messages;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.Instant;
import java.util.UUID;

@Entity
@Document(collection = "messages")
@CompoundIndexes({
        @CompoundIndex(name = "conversation_created", def = "{'conversationId': 1, 'createdAt': 1}"),
        @CompoundIndex(name = "user_conversation", def = "{'userId': 1, 'conversationId': 1}")
})
public class MessageEntity {

    @Id
    private String id;

    @Indexed
    private UUID userId;
    @Indexed
    private String conversationId;
    @Indexed
    private MessageRole role;
    private String content;
    private String audioUrl;
    @Indexed
    private MessageStatus status;
    @Indexed
    private Instant createdAt;
    private Instant updatedAt;
    private Long noteId;
    private UUID assistantId;

    public MessageEntity() {}

    public MessageEntity(UUID userId, String conversationId, MessageRole role, String content) {
        this.userId = userId;
        this.conversationId = conversationId;
        this.role = role;
        this.content = content;
        this.status = MessageStatus.COMPLETED;
        this.createdAt = Instant.now();
    }

    public String getId() {
        return id;
    }

    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }

    public MessageRole getRole() {
        return role;
    }

    public void setRole(MessageRole role) {
        this.role = role;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAudioUrl() {
        return audioUrl;
    }

    public void setAudioUrl(String audioUrl) {
        this.audioUrl = audioUrl;
    }

    public MessageStatus getStatus() {
        return status;
    }

    public void setStatus(MessageStatus status) {
        this.status = status;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Long getNoteId() {
        return noteId;
    }

    public void setNoteId(Long noteId) {
        this.noteId = noteId;
    }

    public UUID getAssistantId() {
        return assistantId;
    }

    public void setAssistantId(UUID assistantId) {
        this.assistantId = assistantId;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }
}