package com.studymate.backend.chat.controller;

import com.studymate.backend.chat.dto.CreateMessageReq;
import com.studymate.backend.chat.service.ChatService;
import com.studymate.backend.member.domain.UserDetail;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Controller;

import java.util.Objects;

@Controller
@Slf4j
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatMessageController {
    private final ChatService chatService;
    @Autowired
    private SimpMessageSendingOperations messagingTemplate;

    @MessageMapping("/chat/message/{chatRoomId}")
    @SendToUser("/sub/chat/room/{chatRoomId}")
    public void chat(StompHeaderAccessor headerAccessor,
                     @DestinationVariable Long chatRoomId,
                     @Payload CreateMessageReq messageRequest) {
        UserDetail userDetail = (UserDetail) Objects.requireNonNull(headerAccessor.getSessionAttributes()).get("userDetail");

//        rabbitTemplate.convertAndSend(CHAT_EXCHANGE_NAME, "room." + chatRoomId, messageRequest);
        messagingTemplate.convertAndSend("/sub/chat/room/" + messageRequest.getChatRoomId(), messageRequest.getContent());

        chatService.saveMessage(userDetail.getMember(), chatRoomId, messageRequest);
        log.info("Message [{}] send by user: {} to chatting room: {}", messageRequest.getContent(), messageRequest.getSender(), chatRoomId);
    }

    // 메세지가 큐에 도착할 때 실행 - rabbitmq 연결
}