package com.sonchasapps.model;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;
import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserEntityResponse {
    private String email;
    private String name;
    private String token;
    private Boolean isPremium;
//    private Long assistant_data;
}
