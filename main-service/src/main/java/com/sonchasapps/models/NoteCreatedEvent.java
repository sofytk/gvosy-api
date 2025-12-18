package com.sonchasapps.models;

public record NoteCreatedEvent(
        Long noteId,
        String messageId,
        String type,
        String title
) {}