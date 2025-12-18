package com.sonchasapps.dto;

import java.util.UUID;


public record KafkaAiRequest(
        String messageId,
        UUID userId,
        String assistantDescription,
        int assistantAge,
        boolean assistantSex,
        String audioUrl
){}

