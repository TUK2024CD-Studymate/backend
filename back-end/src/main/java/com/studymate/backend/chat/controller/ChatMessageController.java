package com.studymate.backend.chat.controller;

import com.studymate.backend.chat.dto.request.CreateMessageRequest;
import com.studymate.backend.chat.dto.response.ChatMessageResponse;
import com.studymate.backend.chat.service.ChattingRoomService;
import com.studymate.backend.member.domain.Member;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/chat")
public class ChatMessageController {
    private final ChattingRoomService chattingRoomService;

    public void chat(StompHeaderAccessor headerAccessor,
                     @DestinationVariable Long chattingRoomId,
                     @Payload CreateMessageRequest messageRequest){

        Member member = (Member) Objects.requireNonNull(headerAccessor.getSessionAttributes()).get("member");

    }
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
