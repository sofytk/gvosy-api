package com.sonchasapps.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
public class AssistantResponse {
    private String name;
    private int age;
    private String description;
    private Boolean sex;
    private String image;

    public AssistantResponse(String name, int age, String description, Boolean sex, String image) {
        this.name = name;
        this.age = age;
        this.description = description;
        this.sex = sex;
        this.image = image;
    }
    public AssistantResponse(){}
}
