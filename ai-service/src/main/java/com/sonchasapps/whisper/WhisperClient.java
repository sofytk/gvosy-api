package com.sonchasapps.whisper;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.io.File;

@Component
public class WhisperClient {
    private final WebClient webClient;

    public WhisperClient(@Value("${whisper.url:http://localhost:9000}") String baseUrl) {
        this.webClient = WebClient.builder().baseUrl(baseUrl).build();
    }

    public String transcribe(File audioFile) {
        FileSystemResource resource = new FileSystemResource(audioFile);

        Mono<String> respMono = webClient.post()
                .uri("/transcribe")
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .body(BodyInserters.fromMultipartData(multipartData(resource)))
                .retrieve()
                .bodyToMono(WhisperResponse.class)
                .map(WhisperResponse::text);

        return respMono.block();
    }

    private MultiValueMap<String, ?> multipartData(FileSystemResource resource) {
        MultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
        map.add("file", resource);
        return map;
    }

    private static class WhisperResponse {
        private String text;
        public String text() { return text; }
        public void setText(String text) { this.text = text; }
    }
}
