package com.sonchasapps.repository.jpa;

import com.sonchasapps.models.jpa.assistants.AssistantEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;
import java.util.UUID;

public interface AssistantRepository extends CrudRepository<AssistantEntity, UUID> {
    Optional<AssistantEntity> findByUserId(UUID userId);
}
