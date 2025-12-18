package com.sonchasapps.dto;

public record PendingTranscription(
        String operationId,
        KafkaAiRequest originalRequest,
        long timestamp
) {

}
