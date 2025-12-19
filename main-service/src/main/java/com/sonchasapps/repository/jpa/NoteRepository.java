package com.sonchasapps.repository.jpa;

import com.sonchasapps.models.jpa.notes.NoteEntity;
import com.sonchasapps.models.jpa.notes.NoteType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface NoteRepository extends JpaRepository<NoteEntity, Long> {

    List<NoteEntity> findByUserIdOrderByCreatedAtDesc(UUID userId);

    List<NoteEntity> findByUserIdAndTypeOrderByCreatedAtDesc(UUID userId, NoteType type);

    List<NoteEntity> findByUserIdAndNoteDateBetweenOrderByNoteDateDesc(
            UUID userId, LocalDate start, LocalDate end
    );

    Optional<NoteEntity> findByMessageId(String messageId);

    @Query("SELECT n FROM NoteEntity n WHERE n.userId = :userId " +
            "AND n.createdAt >= :since " +
            "ORDER BY n.createdAt DESC")
    List<NoteEntity> findRecentNotes(
            @Param("userId") UUID userId,
            @Param("since") Instant since
    );
}
