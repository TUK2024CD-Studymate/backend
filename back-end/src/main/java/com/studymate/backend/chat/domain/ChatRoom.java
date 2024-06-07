package com.studymate.backend.chat.domain;

import com.studymate.backend.global.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;


import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@Entity
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatRoom extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chat_room_id")
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;  // 채팅방 이름 필드 추가

    @OneToMany(mappedBy = "chatRoom", cascade = CascadeType.ALL)
    private final List<ChatMessage> chatMessageList = new ArrayList<>();

    @OneToMany(mappedBy = "chatRoom", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private final List<UserChatRoom> userChatRoomList = new ArrayList<>();

    @Builder
    public ChatRoom(Long id, String name, List<ChatMessage> chatMessageList, List<UserChatRoom> userChatRoomList) {
        this.id = id;
        this.name = name;
        if (chatMessageList != null) {
            this.chatMessageList.addAll(chatMessageList);
        }
        if (userChatRoomList != null) {
            this.userChatRoomList.addAll(userChatRoomList);
        }
    }

}
