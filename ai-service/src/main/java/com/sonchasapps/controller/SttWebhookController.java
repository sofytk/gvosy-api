package com.sonchasapps.controller;

import com.sonchasapps.dto.KafkaAiRequest;
import com.sonchasapps.dto.KafkaAiResponse;
import com.sonchasapps.dto.PendingTranscription;
import com.sonchasapps.kafka.AiResponseProducer;
import com.sonchasapps.llm.LLMClient;
import com.sonchasapps.llm.PromtGeneration;
import com.sonchasapps.service.AiProcessingService;
import com.sonchasapps.service.PendingTranscriptionStore;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/stt")
public class SttWebhookController {

    private final PendingTranscriptionStore pendingStore;
    private final AiProcessingService processingService;

    @Value("${app.callback.enabled:false}")
    private boolean webhookEnabled;

    public SttWebhookController(
            PendingTranscriptionStore pendingStore,
            AiProcessingService processingService) {
        this.pendingStore = pendingStore;
        this.processingService = processingService;
    }

    /**
     * Webhook endpoint для Yandex Cloud
     *
     * В режиме разработки (dev) этот endpoint не используется.
     * В продакшене Yandex будет вызывать этот URL когда транскрипция готова.
    **/

    @PostMapping("/webhook")
    public ResponseEntity<Void> handleWebhook(@RequestBody String payload) {
        try {
            if (!webhookEnabled) {
                System.out.println("Webhook received but webhook mode is disabled (dev mode)");
                return ResponseEntity.ok().build();
            }

            System.out.println("====== WEBHOOK RECEIVED ======");
            System.out.println(payload);
            System.out.println("==============================");

            JSONObject json = new JSONObject(payload);

            String operationId = json.getString("id");
            boolean done = json.optBoolean("done", false);

            Optional<PendingTranscription> pending = pendingStore.get(operationId);

            if (pending.isEmpty()) {
                System.err.println("No pending request found for operation: " + operationId);
                return ResponseEntity.notFound().build();
            }

            KafkaAiRequest originalRequest = pending.get().originalRequest();

            if (json.has("error")) {
                JSONObject error = json.getJSONObject("error");
                String errorMessage = error.optString("message", "Unknown error");
                int errorCode = error.optInt("code", -1);

                System.err.println("STT operation failed: " + errorMessage + " (code: " + errorCode + ")");

                pendingStore.remove(operationId);
                return ResponseEntity.ok().build();
            }
            if (done) {
                if (!json.has("response")) {
                    System.err.println("Operation done but no response field");
                    pendingStore.remove(operationId);
                    return ResponseEntity.ok().build();
                }

                String transcribedText = extractTextFromResponse(json.getJSONObject("response"));

                System.out.println("Transcription completed via webhook: " + transcribedText);

                processingService.processWithLLM(originalRequest, transcribedText);

                pendingStore.remove(operationId);
            } else {
                System.out.println("⏳ Transcription in progress: " + operationId);
            }

            return ResponseEntity.ok().build();

        } catch (Exception e) {
            System.err.println("Error processing webhook: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    private String extractTextFromResponse(JSONObject response) {
        if (!response.has("chunks")) {
            return "";
        }

        JSONArray chunks = response.getJSONArray("chunks");
        StringBuilder fullText = new StringBuilder();

        for (int i = 0; i < chunks.length(); i++) {
            JSONObject chunk = chunks.getJSONObject(i);

            if (!chunk.has("alternatives")) {
                continue;
            }

            JSONArray alternatives = chunk.getJSONArray("alternatives");

            if (alternatives.length() > 0) {
                JSONObject alternative = alternatives.getJSONObject(0);
                String text = alternative.optString("text", "");

                if (!text.isEmpty()) {
                    fullText.append(text);
                    if (i < chunks.length() - 1) {
                        fullText.append(" ");
                    }
                }
            }
        }

        return fullText.toString().trim();
    }
}