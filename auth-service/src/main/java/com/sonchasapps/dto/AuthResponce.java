package com.sonchasapps.dto;

import lombok.Getter;
@Getter

public class AuthResponce {
    private String accessToken;
    private String tokenType = "Bearer";

    public AuthResponce(String accessToken) {
        this.accessToken = accessToken;
    }
}
