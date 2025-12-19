package com.sonchasapps.models.jpa.notes;

public record NoteCreatedEvent(
        Long noteId,
        String messageId,
        String type,
        String title
) {}