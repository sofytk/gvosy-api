package com.sonchasapps.model;


import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;


@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "users_database", indexes = {
        @Index(columnList = "email", name = "idx_users_email", unique = true)
})
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "password_hash", nullable = false)
    private String passwordHash;

    @Builder.Default
    @Column(name = "is_premium", nullable = false)
    private Boolean isPremium = false;

    public String getPasswordHash() {
        return passwordHash;
    }

    @Column(name = "assistant_id")
    private Long assistant_id;

    @Builder.Default
    @Column(name = "created_at", nullable = false)
    private Instant createdAt = Instant.now();
}
