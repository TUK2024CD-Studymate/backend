package com.studymate.backend.chat.error;

public class ChatParticipationNotFoundException extends RuntimeException {
    public ChatParticipationNotFoundException(String message) {
        super(message);
    }
}
