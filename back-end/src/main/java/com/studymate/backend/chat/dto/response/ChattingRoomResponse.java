package com.studymate.backend.chat.dto.response;

import lombok.*;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)

public class ChattingRoomResponse {
    private Long chattingroom_id;
    private String nickname;
}
