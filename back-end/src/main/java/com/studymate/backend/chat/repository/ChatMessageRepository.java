package com.studymate.backend.chat.repository;

import com.studymate.backend.chat.entity.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    List<ChatMessage> findByChattingRoomId(Long chattingRoomId);

    void deleteByChattingRoomId(Long chattingRoomId);
}
