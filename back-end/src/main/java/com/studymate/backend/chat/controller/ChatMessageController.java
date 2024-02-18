package com.studymate.backend.chat.controller;

import com.studymate.backend.chat.dto.response.ChatMessageResponse;
import com.studymate.backend.chat.service.ChattingRoomService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/chats")
public class ChatMessageController {
    private final ChattingRoomService chattingRoomService;

    public ChatMessageController(ChattingRoomService chattingRoomService) {
        this.chattingRoomService = chattingRoomService;
    }

    // 특정 채팅방의 메시지 내역 조회
    @GetMapping("/{chattingRoomId}/messages")
    public ResponseEntity<List<ChatMessageResponse>> getChatMessages(@PathVariable Long chattingRoomId) {
        List<ChatMessageResponse> messages = chattingRoomService.findChatMessage(chattingRoomId);
        return ResponseEntity.ok(messages);
    }
}
