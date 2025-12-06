package com.sonchasapps.service;

import com.sonchasapps.dto.KafkaAiResponse;
import com.sonchasapps.models.NoteEntity;
import com.sonchasapps.repository.NoteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class NoteService {

    private final NoteRepository repo;

    public NoteService(NoteRepository repo) {
        this.repo = repo;
    }

    public void handleNoteCreation(KafkaAiResponse ai) {

        if (ai.getType().equals("other")) return;

        NoteEntity note = new NoteEntity();
        note.setUserId(ai.getUserId());
        note.setCreatedAt(Instant.now());
        note.setType(ai.getType());
        note.setTitle(generateTitle(ai));
        note.setContent(ai.getOriginalText());
        note.setMetadata(ai.getMetadata());

        repo.save(note);
    }

    private String generateTitle(KafkaAiResponse ai) {
        return switch (ai.getType()) {
            case "weekly_plan" -> "Weekly Plan";
            case "project_idea" -> "Project Idea";
            case "todo_list" -> "Todo List";
            case "journal" -> "Journal Entry";
            case "task" -> "Task";
            default -> "Note";
        };
    }
}
