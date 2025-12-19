package com.sonchasapps.repository.mongo;

import com.sonchasapps.models.messages.MessageEntity;
import com.sonchasapps.models.messages.MessageRole;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;


import java.time.Instant;
import java.util.List;
import java.util.UUID;


@Repository
public interface MessageRepository extends MongoRepository<MessageEntity, String> {

    List<MessageEntity> findByConversationIdOrderByCreatedAtAsc(String conversationId);
    List<MessageEntity> findByUserIdOrderByCreatedAtDesc(UUID userId);
    List<MessageEntity> findTop50ByConversationIdOrderByCreatedAtDesc(String conversationId);
    List<MessageEntity> findByConversationIdAndRoleOrderByCreatedAtAsc(
            String conversationId, MessageRole role
    );
    List<MessageEntity> findByUserIdAndCreatedAtBetweenOrderByCreatedAtDesc(
            UUID userId, Instant start, Instant end
    );
    List<MessageEntity> findByUserIdAndContentContainingIgnoreCase(
            UUID userId, String searchTerm
    );
}