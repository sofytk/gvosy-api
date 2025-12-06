package com.sonchasapps.repository;

import com.netflix.spectator.api.Registry;
import com.sonchasapps.models.MessageEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.UUID;

public interface MessageRepository extends MongoRepository<MessageEntity, String> {
    List<MessageEntity> findAllById(UUID userId);

    List<MessageEntity> findByAssistantId(UUID assistantId);
}
