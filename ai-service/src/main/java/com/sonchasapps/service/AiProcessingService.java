package com.sonchasapps.service;



import com.sonchasapps.dto.KafkaAiResponse;
import com.sonchasapps.dto.KafkaAiRequest;
import com.sonchasapps.llm.LLMClient;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@Service
public class AiProcessingService {

    private final AudioTranscriberService transcriber;
    private final NoteClassifier classifier;
    private final LLMClient llmClient;
    private final KafkaTemplate<String, KafkaAiResponse> kafkaTemplate;
    private final String analyzerModel;

    public AiProcessingService(AudioTranscriberService transcriber,
                               NoteClassifier classifier,
                               LLMClient llmClient,
                               KafkaTemplate<String, KafkaAiResponse> kafkaTemplate,
                               @org.springframework.beans.factory.annotation.Value("${llm.analyzerModel:llama-3-8b}") String analyzerModel) {
        this.transcriber = transcriber;
        this.classifier = classifier;
        this.llmClient = llmClient;
        this.kafkaTemplate = kafkaTemplate;
        this.analyzerModel = analyzerModel;
    }

    public void process(KafkaAiRequest request) {
        try {
            String audioUrl = request.audioURL();
            String text = transcriber.transcribeFromUrl(audioUrl);

            String type = classifier.classify(text);

            String analyzePrompt = switch (type) {
                case "weekly_plan" -> buildWeeklyPlanPrompt(text);
                case "project_idea" -> buildProjectIdeaPrompt(text);
                case "todo_list" -> buildTodoPrompt(text);
                case "journal" -> buildJournalPrompt(text);
                case "task" -> buildTaskPrompt(text);
                default -> buildGenericPrompt(text);
            };

            String summary = llmClient.generate(analyzePrompt, analyzerModel);

            Map<String, Object> metadata = new HashMap<>();
            metadata.put("processedAt", Instant.now().toString());
            metadata.put("type", type);

            KafkaAiResponse response = new KafkaAiResponse(
                    request.messageId(),
                    request.userId(),
                    request.assistantId(),
                    text,
                    summary,
                    type,
                    metadata
            );

            kafkaTemplate.send("audio.transcription.response", response);
        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    private String buildWeeklyPlanPrompt(String text) {
        return """
                Ты — ассистент. Преобразуй следующую запись в структурированный план на неделю.
                Выдели задачи и распределяй по дням (Monday..Sunday).
                Дай краткие подзадачи и приоритеты.

                Запись:
                %s
                """.formatted(text);
    }

    private String buildProjectIdeaPrompt(String text) {
        return """
                Ты — ассистент. Пользователь озвучил идею проекта.
                Выдели краткое резюме идеи, цель, 3 ключевые функции, 3 риска, и 5 первых шагов реализации.

                Текст:
                %s
                """.formatted(text);
    }

    private String buildTodoPrompt(String text) {
        return """
                Сформируй структурированный список дел (task, priority, estimate).
                Текст:
                %s
                """.formatted(text);
    }

    private String buildJournalPrompt(String text) {
        return """
                Это дневниковая запись. Сформируй краткое резюме эмоций, 3 вопроса для рефлексии и предложи следующий маленький шаг.
                Текст:
                %s
                """.formatted(text);
    }

    private String buildTaskPrompt(String text) {
        return """
                Выдели задачу, разбей на подзадачи с дедлайнами/оценкой сложности.
                Текст:
                %s
                """.formatted(text);
    }

    private String buildGenericPrompt(String text) {
        return """
                Сформируй краткую сводку (summary) из текста и предложи 3 действия.
                Текст:
                %s
                """.formatted(text);
    }
}

