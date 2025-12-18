package com.sonchasapps.kafka;

import com.sonchasapps.dto.KafkaAiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AiResponseProducer {

    private final KafkaTemplate<String, KafkaAiResponse> kafkaTemplate;

    public void sendResponse(KafkaAiResponse response) {
        kafkaTemplate.send("audio.transcription.response", response);
    }
}

