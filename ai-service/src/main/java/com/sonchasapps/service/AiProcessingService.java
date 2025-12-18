package com.sonchasapps.service;

import com.sonchasapps.dto.KafkaAiResponse;
import com.sonchasapps.dto.KafkaAiRequest;
import com.sonchasapps.kafka.AiResponseProducer;
import com.sonchasapps.llm.LLMClient;
import com.sonchasapps.llm.PromtGeneration;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AiProcessingService {

    private final AudioTranscriberService transcribeAudio;
    private final PendingTranscriptionStore pendingStore;
    private final LLMClient llm;
    private final PromtGeneration promtBuilder;
    private final AiResponseProducer producer;

    public void process(KafkaAiRequest request) {
        try {
            System.out.println("====== PROCESSING REQUEST ======");
            System.out.println("Message ID: " + request.messageId());
            System.out.println("User ID: " + request.userId());
            System.out.println("Audio URL: " + request.audioUrl());
            System.out.println("Mode: " + transcribeAudio.getMode());
            System.out.println("================================");

            String operationId = transcribeAudio.startTranscription(request.audioUrl());

            if (transcribeAudio.isCallbackEnabled()) {
                pendingStore.store(operationId, request);
                System.out.println("Stored for webhook callback: " + operationId);
            }
            else {
                System.out.println("Using polling mode for: " + operationId);
                processWithPolling(request, operationId);
            }

        } catch (Exception e) {
            System.err.println("Failed to process request: " + e.getMessage());
            e.printStackTrace();
            sendErrorResponse(request, e.getMessage());
        }
    }
    private void processWithPolling(KafkaAiRequest request, String operationId) {
        transcribeAudio.waitForCompletion(operationId)
                .thenAccept(text -> {
                    System.out.println("Transcription completed: " + text);
                    processWithLLM(request, text);
                })
                .exceptionally(ex -> {
                    System.err.println("Transcription failed: " + ex.getMessage());
                    ex.printStackTrace();
                    sendErrorResponse(request, ex.getMessage());
                    return null;
                });
    }

    public void processWithLLM(KafkaAiRequest request, String transcribedText) {
        try {
            System.out.println("Processing with LLM...");
            String sex = request.assistantSex() ? "female" : "male";
            String assistantJson = llm.classify(
                    promtBuilder.buildPrompt(
                            transcribedText,
                            request.assistantDescription(),
                            String.valueOf(request.assistantAge()),
                            sex
                    )
            );

            KafkaAiResponse response = new KafkaAiResponse(
                    request.messageId(),
                    request.userId(),
                    transcribedText,
                    assistantJson
            );

            producer.sendResponse(response);
            System.out.println("Sent final response for message: " + request.messageId());

        } catch (Exception e) {
            System.err.println("Error processing with LLM: " + e.getMessage());
            e.printStackTrace();
            sendErrorResponse(request, "LLM processing failed: " + e.getMessage());
        }
    }

    private void sendErrorResponse(KafkaAiRequest request, String errorMessage) {
        System.out.println("Sent error response for message: " + request.messageId());
    }
}
