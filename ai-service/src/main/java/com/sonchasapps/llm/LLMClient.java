package com.sonchasapps.llm;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public class LLMClient {

    private final WebClient webClient;
    private final String provider;
    public LLMClient(@Value("${llm.baseUrl:http://localhost:11434}") String baseUrl,
                     @Value("${llm.provider:ollama}") String provider) {
        this.webClient = WebClient.builder().baseUrl(baseUrl).build();
        this.provider = provider;
    }

    public String generate(String prompt, String model) {
        if ("ollama".equalsIgnoreCase(provider)) {
            return callOllama(prompt, model);
        } else {
            return callGenericWebUi(prompt, model);
        }
    }

    private String callOllama(String prompt, String model) {
        Mono<OllamaResp> resp = webClient.post()
                .uri("/api/generate")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new OllamaReq(model, prompt))
                .retrieve()
                .bodyToMono(OllamaResp.class);
        OllamaResp r = resp.block();
        return r != null ? r.getText() : "";
    }

    private String callGenericWebUi(String prompt, String model) {
        Mono<WebUiResp> resp = webClient.post()
                .uri("/api/v1/generate")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new WebUiReq(prompt, model))
                .retrieve()
                .bodyToMono(WebUiResp.class);
        WebUiResp r = resp.block();
        return r != null ? r.getText() : "";
    }

    private static class OllamaReq {
        public String model;
        public String prompt;
        public OllamaReq(String model, String prompt) { this.model = model; this.prompt = prompt; }
    }
    private static class OllamaResp {
        private String text;
        public String getText(){ return text; }
        public void setText(String text){ this.text = text; }
    }

    private static class WebUiReq { public String inputs; public WebUiReq(String inputs, String model){ this.inputs = inputs; } }
    private static class WebUiResp { private String generated_text; public String getText(){ return generated_text; } public void setGenerated_text(String t){ this.generated_text = t; } }
}
