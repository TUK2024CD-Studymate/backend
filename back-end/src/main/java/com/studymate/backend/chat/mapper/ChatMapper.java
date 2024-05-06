package com.studymate.backend.chat.mapper;

import com.studymate.backend.chat.domain.ChatMessage;
import com.studymate.backend.chat.domain.ChatRoom;
import com.studymate.backend.chat.domain.UserChatRoom;
import com.studymate.backend.chat.dto.ChatMessageRes;
import com.studymate.backend.chat.dto.ChatRoomRes;
import com.studymate.backend.chat.dto.CreateMessageReq;
import com.studymate.backend.member.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static java.time.LocalDateTime.now;

@Component
@RequiredArgsConstructor
public class ChatMapper {

    public UserChatRoom toUserChatRoom(Member member, ChatRoom chatRoom) {
        return UserChatRoom.builder()
                .member(member)
                .chatRoom(chatRoom)
                .build();
    }

    public ChatMessage toChatMessage(Member member, ChatRoom chatRoom, CreateMessageReq request) {
        return ChatMessage.builder()
                .member(member)
                .chatRoom(chatRoom)
                .content(request.getContent())
                .sendDate(now())
                .build();
    }

    public ChatMessageRes toChatMessageDto(ChatMessage chatMessage) {
        return ChatMessageRes.builder()
                .sender(chatMessage.getMember().getNickname())
                .content(chatMessage.getContent())
                .sendDate(chatMessage.getSendDate())
                .build();
    }

    public ChatRoomRes toChatRoomDto(ChatRoom chatRoom, List<Member> otherMembers) {
        return ChatRoomRes.builder()
                .chatRoomId(chatRoom.getId())
                .chatRoomName(chatRoom.getName())
                .members(otherMembers.stream().map(this::toMemberDetail).collect(Collectors.toList()))
                .build();
    }

    private ChatRoomRes.MemberDetail toMemberDetail(Member member) {
        return ChatRoomRes.MemberDetail.builder()
                .id(member.getId())
                .name(member.getName())
                .nickname(member.getNickname())
                .expertiseField(member.getExpertiseField())
                .interests(Collections.singletonList(member.getInterests().getKorean())) // 관심사를 한 개의 String으로 변환
                .isLogin(member.isLogin())
                .build();
    }


    public List<ChatMessageRes> toChatMessageList(List<ChatMessage> messages) {
        return messages.stream().map(this::toChatMessageDto).collect(Collectors.toList());
    }

//    public List<ChatRoomRes> toChatRoomList(List<UserChatRoom> chatRooms) {
//        return chatRooms.stream().map(this::toChatRoomDto).collect(Collectors.toList());
//    }
}

