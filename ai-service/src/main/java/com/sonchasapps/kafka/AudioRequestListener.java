package com.sonchasapps.kafka;


import com.sonchasapps.dto.KafkaAiRequest;
import com.sonchasapps.service.AiProcessingService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class AudioRequestListener {

    private final AiProcessingService processingService;

    public AudioRequestListener(AiProcessingService processingService) {
        this.processingService = processingService;
    }

    @KafkaListener(topics = "audio.transcription.request", groupId = "ai-service-group", containerFactory = "kafkaListenerContainerFactory")
    public void listen(KafkaAiRequest request) {
        processingService.process(request);
    }
}

