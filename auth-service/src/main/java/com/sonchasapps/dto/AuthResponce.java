package com.sonchasapps.dto;

import com.sonchasapps.model.UserEntity;
import com.sonchasapps.model.UserEntityResponse;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class AuthResponce {
    private String accessToken;
    private String tokenType = "Bearer";
    private UserEntityResponse userData;

    public AuthResponce(String accessToken, UserEntityResponse user) {
        this.accessToken = accessToken;
        userData = user;
    }
}
