package com.sonchasapps.service;

import com.sonchasapps.whisper.WhisperClient;
import org.springframework.stereotype.Service;

import java.io.File;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
public class AudioTranscriberService {

    private final WhisperClient whisperClient;

    public AudioTranscriberService(WhisperClient whisperClient) {
        this.whisperClient = whisperClient;
    }

    public String transcribeFromUrl(String audioUrl) throws Exception {
        Path tmp = Files.createTempFile("audio_", ".m4a");
        try (var in = new URL(audioUrl).openStream()) {
            Files.copy(in, tmp, java.nio.file.StandardCopyOption.REPLACE_EXISTING);
        }
        File audioFile = tmp.toFile();
        try {
            String text = whisperClient.transcribe(audioFile);
            return text;
        } finally {
            Files.deleteIfExists(tmp);
        }
    }
}