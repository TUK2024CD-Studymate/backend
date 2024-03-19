package com.studymate.backend.chat.domain;

import lombok.Getter;
import lombok.Setter;


import java.util.Random;

@Getter
@Setter
public class ChatRoom {
    private String roomId;
    private String name;
    public static ChatRoom create(String name) {
        ChatRoom chatRoom = new ChatRoom();
        // 0에서 9999 사이의 랜덤 정수 생성
        int randomNumber = new Random().nextInt(10000);
        // 랜덤 정수를 문자열로 변환하며, 필요한 경우 앞에 0을 붙여 4자리수를 유지
        chatRoom.roomId = String.format("%04d", randomNumber);
        chatRoom.name = name;
        return chatRoom;
    }

}
