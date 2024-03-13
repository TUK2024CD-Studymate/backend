package com.studymate.backend.chat.dto;

import lombok.*;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access =  AccessLevel.PRIVATE)
@Getter
@Setter
@Builder
public class CreateMessageReq {
    private String content;
    private String sender;  // username
}