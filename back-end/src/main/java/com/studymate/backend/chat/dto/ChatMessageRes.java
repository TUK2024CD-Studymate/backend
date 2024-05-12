package com.studymate.backend.chat.dto;

import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access =  AccessLevel.PRIVATE)
@Getter
@Builder
public class ChatMessageRes {
    private Long messageId;
    private String sender;
    private String content;
    private LocalDateTime sendDate;
    private boolean isRead;  // 읽음 여부
}
