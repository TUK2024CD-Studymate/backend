package com.studymate.backend.chat.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
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

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm", timezone = "Asia/Seoul")
    private LocalDateTime sendDate;
    private boolean isRead;  // 읽음 여부
    private String profileImageUrl;
}
