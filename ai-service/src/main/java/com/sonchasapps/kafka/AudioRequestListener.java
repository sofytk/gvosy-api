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

    @KafkaListener(topics = "audio.transcription.request",
            groupId = "ai-service",
            containerFactory = "kafkaListenerContainerFactory")
    public void listen(KafkaAiRequest request) {
        try {
            System.out.println("====== RECEIVED KAFKA MESSAGE ======");
            System.out.println("Message ID: " + request.messageId());
            System.out.println("User ID: " + request.userId());
            System.out.println("Audio URL: " + request.audioUrl());
            System.out.println("====================================");
            processingService.process(request);

        }catch(Exception e){
            System.out.println("======LISTEN KAFKA ERROR ====== \n" + e.getMessage());
        }
    }
}

