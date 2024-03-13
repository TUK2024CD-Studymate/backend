package com.studymate.backend.chat.controller;

import com.studymate.backend.chat.domain.ChatMessage;
import com.studymate.backend.chat.dto.CreateMessageReq;
import com.studymate.backend.chat.service.ChatService;
import com.studymate.backend.member.domain.UserDetail;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@Controller
@Slf4j
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class MessageController {
    private final ChatService chatService;

    @MessageMapping("chat.message.{chatRoomId}")
    public void chat(StompHeaderAccessor headerAccessor,
                     @DestinationVariable Long chatRoomId,
                     @Payload CreateMessageReq messageRequest) {
        UserDetail userDetail = (UserDetail) Objects.requireNonNull(headerAccessor.getSessionAttributes()).get("userDetail");

//        rabbitTemplate.convertAndSend(CHAT_EXCHANGE_NAME, "room." + chatRoomId, messageRequest);

        chatService.saveMessage(userDetail.getMember(), chatRoomId, messageRequest);
        log.info("Message [{}] send by user: {} to chatting room: {}", messageRequest.getContent(), messageRequest.getSender(), chatRoomId);
    }

    // 메세지가 큐에 도착할 때 실행 - rabbitmq 연결
}