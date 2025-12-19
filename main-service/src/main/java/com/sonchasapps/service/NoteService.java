package com.sonchasapps.service;

import com.sonchasapps.dto.KafkaAiResponse;
import com.sonchasapps.models.jpa.notes.NoteCreatedEvent;
import com.sonchasapps.models.jpa.notes.NoteData;
import com.sonchasapps.models.jpa.notes.NoteEntity;
import com.sonchasapps.models.jpa.notes.NoteType;
import com.sonchasapps.repository.jpa.NoteRepository;
import org.springframework.stereotype.Service;
import java.time.Instant;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Service
public class NoteService {

    private final NoteRepository repo;
    private final WebSocketPushService pushService;
    private final AssistantService service;

    public NoteService(NoteRepository repo, WebSocketPushService pushService, AssistantService service) {
        this.repo = repo;
        this.pushService = pushService;
        this.service = service;
    }

    public void handleNoteCreation(KafkaAiResponse ai) {
        try {
            UUID assistantId = service.getAssistantIdByUserId(ai.getUserId());
            System.out.println("====== CREATING NOTE ======");
            System.out.println("Message ID: " + ai.getMessageId());
            System.out.println("User ID: " + ai.getUserId());
            System.out.println("Assistant ID: " + assistantId);
            System.out.println("Summary (JSON): " + ai.getSummary());

            NoteData noteData = new NoteData();
            noteData.parse(ai.getSummary());

            System.out.println("Classification: " + noteData.getClassification());


            if (noteData.getClassification() == NoteType.OTHER) {
                System.out.println("Type is 'other', skipping note creation");

                pushService.sendToAssistant(
                        assistantId,
                        new NoteCreatedEvent(
                                null,
                                ai.getMessageId(),
                                "other",
                                noteData.getAssistantReply()
                        )
                );

                return;
            }


            NoteEntity note = new NoteEntity();
            note.setUserId(ai.getUserId());
            note.setMessageId(ai.getMessageId());
            note.setCreatedAt(Instant.now());
            note.setType(noteData.getClassification());
            note.setTitle(generateTitle(noteData));
            note.setContent(ai.getOriginalText());
            note.setAssistantReply(noteData.getAssistantReply());
            note.setMetadata(noteData.getMetadataJson());


            if (noteData.getActions() != null && !noteData.getActions().isEmpty()) {
                note.setSummary(String.join("; ", noteData.getActions()));
            } else {
                note.setSummary(noteData.getSummary());
            }


            extractAdditionalFields(note, noteData);


            note = repo.save(note);

            System.out.println("Note saved! ID: " + note.getId());

            pushService.sendToAssistant(
                    assistantId,
                    new NoteCreatedEvent(
                            note.getId(),
                            ai.getMessageId(),
                            note.getType().name(),
                            note.getTitle()
                    )
            );

        } catch (Exception e) {
            System.err.println("Failed to create note: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private String generateTitle(NoteData noteData) {
        return switch (noteData.getClassification()) {
            case WEEKLY_PLAN -> {
                if (noteData.getWeeklyPlan() != null && noteData.getWeeklyPlan().getWeek() != null) {
                    yield "Weekly Plan - " + noteData.getWeeklyPlan().getWeek();
                }
                yield "Weekly Plan";
            }
            case PROJECT_IDEA -> {
                if (noteData.getProjectIdea() != null && noteData.getProjectIdea().getProjectName() != null) {
                    yield noteData.getProjectIdea().getProjectName();
                }
                yield "Project Idea";
            }
            case TODO_LIST -> "Todo List - " + LocalDate.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
            case JOURNAL -> {
                if (noteData.getJournal() != null && noteData.getJournal().getDate() != null) {
                    yield "Journal - " + noteData.getJournal().getDate();
                }
                yield "Journal Entry";
            }
            case TASK -> {
                if (noteData.getTask() != null && noteData.getTask().getTaskName() != null) {
                    yield noteData.getTask().getTaskName();
                }
                yield "Task";
            }
            case OTHER -> "Note";
        };
    }

    private void extractAdditionalFields(NoteEntity note, NoteData noteData) {
        if (noteData.getJournal() != null) {
            if (noteData.getJournal().getDate() != null) {
                try {
                    note.setNoteDate(LocalDate.parse(noteData.getJournal().getDate()));
                } catch (Exception e) {
                    System.err.println("Failed to parse date: " + noteData.getJournal().getDate());
                }
            }
            note.setMood(noteData.getJournal().getMood());
        }
    }
}
