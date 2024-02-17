package com.studymate.backend.chat.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@Setter
@Builder
@Schema(description = "ChatMessage")
public class CreateMessageRequest {

    @Schema(description = "채팅 내용", nullable = false, example = "뭐해")
    private String content;

    @Schema(description = "보낸 사람", nullable = false, example = "최지혜")
    private String sender;
}
