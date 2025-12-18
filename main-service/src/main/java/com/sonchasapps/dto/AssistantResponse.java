package com.sonchasapps.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


public class AssistantResponse {
    private String name;
    private int age;
    private String description;
    private boolean sex;
    private String image;

    public AssistantResponse(String name, int age, String description, boolean sex, String image) {
        this.name = name;
        this.age = age;
        this.description = description;
        this.sex = sex;
        this.image = image;
    }
    public AssistantResponse(){}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public boolean getSex() {
        return sex;
    }

    public void setSex(boolean sex) {
        this.sex = sex;
    }
}
