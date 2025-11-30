package com.sonchasapps.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class AssistantRequest {
    private String assistantName;
    private int assistantAge;
    private String assistantDescription;
    private Boolean assistantSex;

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

    public String getAssistantDescription() {
        return assistantDescription;
    }

    public void setAssistantDescription(String assistantDescription) {
        this.assistantDescription = assistantDescription;
    }

    public Boolean getAssistantSex() {
        return assistantSex;
    }

    public void setAssistantSex(Boolean assistantSex) {
        this.assistantSex = assistantSex;
    }
}
