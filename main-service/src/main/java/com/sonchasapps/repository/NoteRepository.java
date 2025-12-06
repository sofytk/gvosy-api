package com.sonchasapps.repository;

import com.sonchasapps.models.NoteEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.UUID;

public interface NoteRepository extends MongoRepository<NoteEntity, UUID> {
}
