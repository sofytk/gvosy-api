package com.sonchasapps.dto;

public record AudioUploadResponse(String audioId, String messageId) {
    @Override
    public String audioId() {
        return audioId;
    }

    @Override
    public String  messageId() {
        return messageId;
    }
}
