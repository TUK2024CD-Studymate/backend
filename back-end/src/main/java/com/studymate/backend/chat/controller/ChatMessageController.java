package com.studymate.backend.chat.controller;

import com.studymate.backend.chat.domain.ChatMessage;
import com.studymate.backend.chat.dto.ChatMessageRes;
import com.studymate.backend.chat.dto.CreateMessageReq;
import com.studymate.backend.chat.service.ChatService;
import com.studymate.backend.chat.service.MessageService;
import com.studymate.backend.member.domain.Member;
import com.studymate.backend.member.MemberRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.Objects;

import static com.studymate.backend.global.constant.RabbitMQ.CHAT_EXCHANGE_NAME;
import static com.studymate.backend.global.constant.RabbitMQ.CHAT_QUEUE_NAME;

@Controller
@Slf4j
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatMessageController {
    private final ChatService chatService;
    private final MemberRepository memberRepository;
    private final MessageService messageService;
    private final RabbitTemplate rabbitTemplate;

    @MessageMapping("chat.message.{chatRoomId}")
    public void chat(StompHeaderAccessor headerAccessor,
                     @DestinationVariable Long chatRoomId,
                     @Payload CreateMessageReq messageRequest) {
        Authentication authentication = (Authentication) Objects.requireNonNull(headerAccessor.getUser());
        User user = (User) authentication.getPrincipal();
        String email = user.getUsername();
        Member member = memberFilter(email);

        if (member == null) {
            log.warn("Failed to find member with email: {}", email);
            return;
        }

        if (messageRequest.getType() == ChatMessage.MessageType.ENTER) {
            handleEnterTypeTriggers(chatRoomId, member);
        } else {
            handleOtherTypeMessages(chatRoomId, messageRequest, member);
        }
    }

    private void handleEnterTypeTriggers(Long chatRoomId, Member member) {
        List<ChatMessageRes> messages = chatService.findChatMessage(chatRoomId, member.getId());
        if (!messages.isEmpty()) {
            ChatMessageRes lastMessage = messages.get(messages.size() - 1);
            String lastMessageId = lastMessage.getMessageId().toString();
            messageService.updateUserReadPosition(chatRoomId.toString(), member.getId().toString(), lastMessageId, true);
        }
    }

    private void handleOtherTypeMessages(Long chatRoomId, CreateMessageReq messageRequest, Member member) {
        rabbitTemplate.convertAndSend(CHAT_EXCHANGE_NAME, "room." + chatRoomId, messageRequest);
        chatService.saveMessage(member, chatRoomId, messageRequest);
    }

    private Member memberFilter(String email) {
        return memberRepository.findByEmail(email);
    }

    // 메세지가 큐에 도착할 때 실행
    @RabbitListener(queues = CHAT_QUEUE_NAME)
    public void receive(CreateMessageReq messageRequest) {
        log.info("message.getText = {}", messageRequest.getContent());
    }
}
