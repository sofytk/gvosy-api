package com.sonchasapps.kafka;

import com.sonchasapps.dto.KafkaAudioRequest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class AudioRequestProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public AudioRequestProducer(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendAudioRequest(KafkaAudioRequest request) {
        kafkaTemplate.send("audio.transcription.request", request);
    }
}

