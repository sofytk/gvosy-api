package com.sonchasapps.kafka;

import com.sonchasapps.dto.KafkaAiResponse;
import org.json.JSONObject;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class TranscriptionConsumer {

    @KafkaListener(topics = "${kafka.topic.transcriptions:transcriptions}", groupId = "transcription-processors")
    public void consumeTranscription(String message) {
        try {
            System.out.println("Received transcription from Kafka: " + message);

            JSONObject json = new JSONObject(message);

            String operationId = json.getString("operationId");
            String text = json.getString("text");
            long timestamp = json.getLong("timestamp");

            // Обрабатываем результат
            processTranscription(operationId, text, timestamp);

        } catch (Exception e) {
            System.err.println("Error processing transcription message: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @KafkaListener(topics = "${kafka.topic.transcription-errors:transcription-errors}", groupId = "transcription-processors")
    public void consumeError(String message) {
        try {
            System.out.println("Received transcription error from Kafka: " + message);

            JSONObject json = new JSONObject(message);

            String operationId = json.getString("operationId");
            String errorMessage = json.getString("errorMessage");

            processError(operationId, errorMessage);

        } catch (Exception e) {
            System.err.println("Error processing error message: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void processTranscription(String operationId, String text, long timestamp) {
        System.out.println("Processing transcription " + operationId + ": " + text);
//        KafkaAiResponse response = new KafkaAiResponse(
//                request.messageId(),
//                request.userId(),
//                UUID.randomUUID(),
//                text,
//                assistantJson
//        );
//
//        producer.sendResponse(response);
        // Ваша бизнес-логика:
        // - Сохранить в другую систему
        // - Отправить уведомление
        // - Запустить следующий этап обработки
        // - И т.д.
    }

    private void processError(String operationId, String errorMessage) {
        System.err.println("Processing transcription error " + operationId + ": " + errorMessage);
        // Ваша обработка ошибок:
        // - Отправить алерт
        // - Повторить попытку
        // - И т.д.
    }
}
