package com.studymate.backend.chat.controller;

import com.studymate.backend.chat.dto.CreateMessageReq;
import com.studymate.backend.chat.service.ChatService;
import com.studymate.backend.member.domain.Member;
import com.studymate.backend.member.MemberRepository;
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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;

import java.util.Objects;

@Controller
@Slf4j
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatMessageController {
    private final ChatService chatService;
    private final MemberRepository memberRepository;
    @Autowired
    private SimpMessageSendingOperations messagingTemplate;

    @MessageMapping("/chat/message/{chatRoomId}")
    @SendToUser("/sub/chat/room/{chatRoomId}")
    public void chat(StompHeaderAccessor headerAccessor,
                     @DestinationVariable Long chatRoomId,
                     @Payload CreateMessageReq messageRequest) {
        Authentication authentication = (Authentication) Objects.requireNonNull(headerAccessor.getUser());
        User user = (User) authentication.getPrincipal();
        String email = user.getUsername();
        Member member = memberRepository.findByEmail(email);

        if (member == null) {
            log.warn("Failed to find member with email: {}", email);
            return;
        }

        messagingTemplate.convertAndSend("/sub/chat/room/" + chatRoomId, messageRequest);
        chatService.saveMessage(member, chatRoomId, messageRequest);
        log.info("Message [{}] sent by user: {} to chat room: {}", messageRequest.getContent(), email, chatRoomId);
    }
}
