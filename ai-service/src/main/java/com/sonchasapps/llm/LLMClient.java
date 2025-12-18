package com.sonchasapps.llm;

import okhttp3.*;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class LLMClient {

    private final OkHttpClient http = new OkHttpClient();
    private static final String OLLAMA_URL = "https://ollama.com/api/chat";
    @Value("${OLLAMA_API:apikey}")
    private String apiKey;

    public String classify(String prompt) {
        try {
            JSONObject body = new JSONObject()
                    .put("model", "gpt-oss:120b-cloud")
                    .put("stream", false)
                    .put("messages", new org.json.JSONArray()
                            .put(new JSONObject()
                                    .put("role", "user")
                                    .put("content", prompt)));

            Request request = new Request.Builder()
                    .url(OLLAMA_URL)
                    .addHeader("Authorization", "Bearer " + apiKey)
                    .post(RequestBody.create(
                            body.toString(),
                            MediaType.parse("application/json")
                    ))
                    .build();

            Response resp = http.newCall(request).execute();
            JSONObject json = new JSONObject(resp.body().string());
            return json.getJSONObject("message").getString("content");

        } catch (Exception e) {
            throw new RuntimeException("LLM error: " + e.getMessage(), e);
        }
    }
}