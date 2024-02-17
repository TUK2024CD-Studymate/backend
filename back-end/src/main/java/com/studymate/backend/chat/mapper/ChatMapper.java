package com.studymate.backend.chat.mapper;

import com.studymate.backend.chat.dto.request.CreateMessageRequest;
import com.studymate.backend.chat.dto.response.ChatMessageResponse;
import com.studymate.backend.chat.dto.response.ChattingRoomResponse;
import com.studymate.backend.chat.entity.ChatMessage;
import com.studymate.backend.chat.entity.ChatParticipation;
import com.studymate.backend.chat.entity.ChattingRoom;
import com.studymate.backend.member.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ChatMapper {

    public ChatParticipation toChatParticipation(ChattingRoom chattingRoom, Member member){
        return ChatParticipation.builder()
                .member(member)
                .chattingRoom(chattingRoom)
                .build();
    }

    public ChatMessage toChatMessage(Member member, ChattingRoom chattingRoom, CreateMessageRequest request){
        return ChatMessage.builder()
                .member(member)
                .chattingRoom(chattingRoom)
                .content(request.getContent())
                .sendTime(LocalDateTime.now())
                .build();
    }

    public ChatMessageResponse toChatMessageDto(ChatMessage chatMessage){
        return ChatMessageResponse.builder()
                .sender(chatMessage.getMember().getNickname())
                .content(chatMessage.getContent())
                .sendTime(chatMessage.getSendTime())
                .build();
    }

    public ChattingRoomResponse toChattingRoomDto(ChatParticipation chatParticipation){
        return ChattingRoomResponse.builder()
                .chattingroom_id(chatParticipation.getChattingRoom().getId())
                .nickname(chatParticipation.getMember().getNickname())
                .build();
    }

    public List<ChatMessageResponse> toChatMessageList(List<ChatMessage> messages){
        return messages.stream().map(this::toChatMessageDto).collect(Collectors.toList());
    }

    public List<ChattingRoomResponse> toChattingRoomList(List<ChatParticipation> chattingRooms){
        return chattingRooms.stream().map(this::toChattingRoomDto).collect(Collectors.toList());
    }
}
