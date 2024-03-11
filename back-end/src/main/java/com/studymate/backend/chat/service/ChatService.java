package com.studymate.backend.chat.service;

import com.studymate.backend.chat.domain.ChatRoom;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class ChatService {

    private Map<Long, ChatRoom> chatRooms;

    @PostConstruct
    //의존관게 주입완료되면 실행되는 코드
    private void init(){
        chatRooms = new LinkedHashMap<>();
    }

    //채팅방 불러오기 - 채팅방 최신 생성 순으로 반환
    public List<ChatRoom> findAllRoom(){
        List<ChatRoom> result = new ArrayList<>(chatRooms.values());
        Collections.reverse(result);

        return result;
    }

    // 채팅방 하나 불러오기
    public ChatRoom findByID(String roomId){
        return chatRooms.get(roomId);
    }

    public ChatRoom createRoom(String name) {
        ChatRoom chatRoom = ChatRoom.create(name);
        chatRooms.put(chatRoom.getId(), chatRoom);
        return chatRoom;
    }
}
