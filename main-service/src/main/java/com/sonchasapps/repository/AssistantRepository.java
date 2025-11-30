package com.sonchasapps.repository;

import com.sonchasapps.models.AssistantEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;
import java.util.UUID;

public interface AssistantRepository extends CrudRepository<AssistantEntity, UUID> {
    Optional<AssistantEntity> findByUserId(UUID userId);
}
