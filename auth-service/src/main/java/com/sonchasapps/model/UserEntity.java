package com.sonchasapps.model;


import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;


@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users_database", indexes = {
        @Index(columnList = "email", name = "idx_users_email", unique = true)
})
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public UserEntity(String email, String name, String passwordHash) {
        this.email = email;
        this.name = name;
        this.passwordHash = passwordHash;
    }

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "password_hash", nullable = false)
    private String passwordHash;

    @Column(name = "is_premium", nullable = false)
    private Boolean isPremium = false;

    public String getPasswordHash() {
        return passwordHash;
    }
    @Column(name = "assistant_id")
    private Long assistant_id;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt = Instant.now();
}
