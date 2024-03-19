package com.studymate.backend.chat.controller;

import com.studymate.backend.chat.domain.ChatMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@RequiredArgsConstructor
@Controller
public class ChatController {

    @Autowired
    private SimpMessageSendingOperations messagingTemplate;

    @MessageMapping("/chat/message")
    @SendToUser("/sub/chat/room/{roomID}")
    public void message(ChatMessage message) {
        log.info("# roomId = {}", message.getRoomId());
        if (ChatMessage.MessageType.ENTER.equals(message.getType())){
            message.setMessage(message.getSender() + "님이 입장하셨습니다.");
        }
        // Log what message will be sent
        log.info("# Sending message to /sub/chat/room/{}: {}", message.getRoomId(), message);

        // Perform the actual sending of the message
        messagingTemplate.convertAndSend("/sub/chat/room/" + message.getRoomId(), message);

        // Log after attempting to send the message
        log.info("# Message sent to /sub/chat/room/{}", message.getRoomId());
    }
//    @MessageMapping("/chat/{roomID}")
//    @SendToUser("/topic/chat/room/{roomID}") // 메서드가 반환하는 객체를 지정된 대상으로 전송
//    public ChatMessage message(@DestinationVariable String roomID, ChatMessage message) {
//        log.info("# roomId = {}", roomID);
//        if (ChatMessage.MessageType.ENTER.equals(message.getType())){
//            message.setMessage(message.getSender() + "님이 입장하셨습니다.");
//        }
//        // Log what message will be sent
//        log.info("# Sending message to /topic/chat/room/{}: {}", roomID, message);
//
//        // 반환값이 /topic/chat/room/{roomID}로 자동 전송됩니다.
//        return message;
//    }
}
