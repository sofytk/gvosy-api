package com.sonchasapps.kafka;

import com.sonchasapps.dto.KafkaAudioRequest;
import lombok.AllArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AudioRequestProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void sendAudioRequest(KafkaAudioRequest request) {
        kafkaTemplate.send("audio.transcription.request", request);
    }
}

