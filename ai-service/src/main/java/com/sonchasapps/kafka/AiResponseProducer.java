package com.sonchasapps.kafka;

import com.sonchasapps.dto.KafkaAiResponse;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class AiResponseProducer {

    private final KafkaTemplate<String, KafkaAiResponse> kafkaTemplate;

    public AiResponseProducer(KafkaTemplate<String, KafkaAiResponse> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendResponse(KafkaAiResponse response) {
        kafkaTemplate.send("audio.transcription.response", response);
    }
}

