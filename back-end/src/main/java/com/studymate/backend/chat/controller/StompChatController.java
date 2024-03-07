package com.studymate.backend.chat.controller;

import com.studymate.backend.chat.dto.request.CreateMessageRequest;
import com.studymate.backend.chat.dto.response.ChatMessageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class StompChatController {
    private final SimpMessagingTemplate template;

    @MessageMapping(value = "/chat/enter")
    public void enter(ChatMessageResponse content){
        content.setContent(content.getSender() + "님이 채팅방에 참여하였습니다.");
        template.convertAndSend("/sub/chat/room/" + content.getChattingroom_id(), content);
    }

    @MessageMapping(value = "/chat/message")
    public void message(ChatMessageResponse content){
        template.convertAndSend("/sub/chat/room/" + content.getChattingroom_id(), content);
    }

}
