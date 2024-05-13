package com.studymate.backend.chat.dto;

import com.studymate.backend.chat.domain.ChatMessage;
import lombok.*;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access =  AccessLevel.PRIVATE)
@Getter
@Setter
@Builder
public class CreateMessageReq {
    private String content;
    private String sender;  // username
    private Long chatRoomId;
    private ChatMessage.MessageType type;  // 메시지 타입 필드 추가

}