package com.studymate.backend.chat.domain;

import jakarta.persistence.*;
import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@AllArgsConstructor
public class ChatRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chatRoom_id")
    private Long id;

    private String nickname;

    public static ChatRoom create(String nickname) {
        return ChatRoom.builder()
                .nickname(nickname)
                .build();
    }
}
