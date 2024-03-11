package com.studymate.backend.chat.controller;

import com.studymate.backend.chat.domain.Chat;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MessageController {

    private final SimpMessageSendingOperations sendingOperations;

    @MessageMapping("/chat/message")
    public void enter(Chat chat){
        if (Chat.MessageType.ENTER.equals(chat.getType())){
            chat.setMessage(chat.getSender() + "님이 입장하였습니다.");
        }
        sendingOperations.convertAndSend("/send/chat/room" + chat.getId(), chat);
    }
}
