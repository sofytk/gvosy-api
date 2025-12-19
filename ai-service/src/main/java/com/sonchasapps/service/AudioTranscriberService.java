package com.sonchasapps.service;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import okhttp3.*;
import java.io.IOException;
import java.util.concurrent.CompletableFuture;

@Component
public class AudioTranscriberService {

    private final OkHttpClient http = new OkHttpClient();

    private static final String STT_URL = "https://transcribe.api.cloud.yandex.net/speech/stt/v2/longRunningRecognize";
    private static final String OPERATION_URL = "https://operation.api.cloud.yandex.net/operations/";

    @Value("${YANDEX_API}")
    private String apiKey;

    @Value("${app.callback.url:}")
    private String callbackUrl;

    @Value("${app.callback.enabled:false}")
    private boolean callbackEnabled;

    @Value("${app.mode:polling}")
    private String mode;

    public String startTranscription(String audioUri) {
        try {
            if (audioUri == null || audioUri.trim().isEmpty()) {
                throw new IllegalArgumentException("Audio URI is null or empty");
            }

            if (!audioUri.startsWith("http://") && !audioUri.startsWith("https://")) {
                throw new IllegalArgumentException("Audio URI must start with http:// or https://");
            }

            System.out.println("Starting transcription for URI: " + audioUri);
            System.out.println("Mode: " + mode);

            return sendRecognitionRequest(audioUri);

        } catch (Exception e) {
            System.err.println("Failed to start transcription: " + e.getMessage());
            throw new RuntimeException("STT error: " + e.getMessage(), e);
        }
    }

    private String sendRecognitionRequest(String uri) throws IOException {
        JSONObject body = new JSONObject()
                .put("config", new JSONObject()
                        .put("specification", new JSONObject()
                                .put("languageCode", "ru-RU")
                                .put("model", "general")
                                .put("profanityFilter", false)
                                .put("literatureText", false)
                                .put("audioEncoding", "MP3")))
                .put("audio", new JSONObject()
                        .put("uri", uri));

        if (callbackEnabled && callbackUrl != null && !callbackUrl.isEmpty()) {
            body.put("callbackUrl", callbackUrl);
            System.out.println("Webhook mode - callback URL: " + callbackUrl);
        } else {
            System.out.println("Polling mode - no callback");
        }

        Request request = new Request.Builder()
                .url(STT_URL)
                .addHeader("Authorization", "Api-Key " + apiKey)
                .post(RequestBody.create(body.toString(),
                        MediaType.parse("application/json")))
                .build();

        try (Response response = http.newCall(request).execute()) {
            String responseBody = response.body().string();

            System.out.println("STT API Response [" + response.code() + "]: " + responseBody);

            if (!response.isSuccessful()) {
                throw new IOException("STT API error [" + response.code() + "]: " + responseBody);
            }

            JSONObject obj = new JSONObject(responseBody);

            if (!obj.has("id")) {
                throw new IOException("Response missing 'id' field: " + responseBody);
            }

            String operationId = obj.getString("id");
            System.out.println("Started transcription with operation ID: " + operationId);

            return operationId;

        } catch (JSONException e) {
            throw new IOException("Failed to parse JSON response", e);
        }
    }

    public CompletableFuture<String> waitForCompletion(String operationId) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return pollOperation(operationId);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    private String pollOperation(String operationId) throws Exception {
        int attempts = 0;
        int maxAttempts = 100;

        System.out.println("Starting polling for operation: " + operationId);

        while (attempts < maxAttempts) {
            Request request = new Request.Builder()
                    .url(OPERATION_URL + operationId)
                    .addHeader("Authorization", "Api-Key " + apiKey)
                    .build();

            try (Response response = http.newCall(request).execute()) {
                String responseBody = response.body().string();
                JSONObject json = new JSONObject(responseBody);

                if (json.has("error")) {
                    String errorMsg = json.getJSONObject("error").optString("message", "Unknown error");
                    throw new RuntimeException("STT error: " + errorMsg);
                }

                if (json.optBoolean("done", false)) {
                    System.out.println("Transcription completed after " + (attempts * 1.5) + " seconds");
                    return extractText(json);
                }

                System.out.println("Polling attempt " + (attempts + 1) + "/" + maxAttempts);
                Thread.sleep(1500);
                attempts++;
            }
        }

        throw new RuntimeException("Operation timeout after " + (maxAttempts * 1.5) + " seconds");
    }

    private String extractText(JSONObject json) {
        if (!json.has("response")) {
            return "";
        }

        JSONObject response = json.getJSONObject("response");

        if (!response.has("chunks")) {
            return "";
        }

        JSONArray chunks = response.getJSONArray("chunks");
        StringBuilder text = new StringBuilder();

        for (int i = 0; i < chunks.length(); i++) {
            JSONObject chunk = chunks.getJSONObject(i);

            if (!chunk.has("alternatives")) {
                continue;
            }

            JSONArray alternatives = chunk.getJSONArray("alternatives");

            if (alternatives.length() > 0) {
                String chunkText = alternatives.getJSONObject(0).optString("text", "");

                if (!chunkText.isEmpty()) {
                    text.append(chunkText);
                    if (i < chunks.length() - 1) {
                        text.append(" ");
                    }
                }
            }
        }

        return text.toString().trim();
    }

    public boolean isCallbackEnabled() {
        return callbackEnabled;
    }

    public String getMode() {
        return mode;
    }
}

