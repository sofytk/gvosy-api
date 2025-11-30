package com.sonchasapps.models;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "assistants_database", indexes = {
        @Index(columnList = "user_id", name = "idx_user_id", unique = true)
})
public class AssistantEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Column(name = "userId", nullable = false)
    private UUID userId;
    @Column(name = "name", nullable = false)
    private String assistantName;
    @Column(name = "age", nullable = false)
    private int assistantAge;
    @Column(name = "description", nullable = false)
    private String assistantDesc;
    @Column(name = "sex", nullable = false)
    private Boolean assistantSex;
    @Column(name = "img")
    private String assistantImg;
    @Column(name = "messageId")
    private Long assistantMessagesId;

    public AssistantEntity(String assistantName, int assistantAge, Boolean assistantSex, String assistantDescription, UUID userId) {
        this.assistantName = assistantName;
        this.assistantAge = assistantAge;
        this.assistantDesc = assistantDescription;
        this.assistantSex = assistantSex;
        this.userId = userId;
    }


    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getAssistantName() {
        return assistantName;
    }

    public void setAssistantName(String assistantName) {
        this.assistantName = assistantName;
    }

    public int getAssistantAge() {
        return assistantAge;
    }

    public void setAssistantAge(int assistantAge) {
        this.assistantAge = assistantAge;
    }

    public String getAssistantDesc() {
        return assistantDesc;
    }

    public void setAssistantDesc(String assistantDesc) {
        this.assistantDesc = assistantDesc;
    }

    public Boolean getAssistantSex() {
        return assistantSex;
    }

    public void setAssistantSex(Boolean assistantSex) {
        this.assistantSex = assistantSex;
    }

    public String getAssistantImg() {
        return assistantImg;
    }

    public void setAssistantImg(String assistantImg) {
        this.assistantImg = assistantImg;
    }

    public Long getAssistantMessagesId() {
        return assistantMessagesId;
    }

    public void setAssistantMessagesId(Long assistantMessagesId) {
        this.assistantMessagesId = assistantMessagesId;
    }
}
