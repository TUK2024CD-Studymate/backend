package com.studymate.backend.chat.mapper;

import com.studymate.backend.chat.domain.ChatMessage;
import com.studymate.backend.chat.domain.ChatRoom;
import com.studymate.backend.chat.domain.UserChatRoom;
import com.studymate.backend.chat.dto.ChatMessageRes;
import com.studymate.backend.chat.dto.ChatRoomRes;
import com.studymate.backend.chat.dto.CreateMessageReq;
import com.studymate.backend.member.domain.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static java.time.LocalDateTime.now;

@Slf4j
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
                .messageId(chatMessage.getId())
                .sender(chatMessage.getMember().getNickname())
                .content(chatMessage.getContent())
                .sendDate(chatMessage.getSendDate())
                .isRead(chatMessage.isRead())  // 읽음 상태 추가
                .build();
    }


    public ChatRoomRes toChatRoomDto(ChatRoom chatRoom, List<Member> otherMembers, Long unreadMessageCount) {
        return ChatRoomRes.builder()
                .chatRoomId(chatRoom.getId())
                .chatRoomName(chatRoom.getName())
                .members(otherMembers.stream().map(this::toMemberDetail).collect(Collectors.toList()))
                .unreadMessageCount(unreadMessageCount) // 읽지 않은 메시지 수를 DTO에 포함
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


    public List<ChatMessageRes> toChatMessageList(List<ChatMessage> messages, String lastReadMessageId) {
        long lastReadId = 0; // 기본값으로 설정

        // lastReadMessageId가 null이 아닌 경우에만 숫자로 변환
        if (lastReadMessageId != null && !lastReadMessageId.isEmpty()) {
            try {
                lastReadId = Long.parseLong(lastReadMessageId);
            } catch (NumberFormatException e) {
                // 로그 출력 또는 적절한 예외 처리
                log.error("Failed to parse lastReadMessageId: {}", lastReadMessageId, e);
                throw new IllegalArgumentException("Invalid lastReadMessageId: " + lastReadMessageId);
            }
        }

        long finalLastReadId = lastReadId;
        return messages.stream()
                .map(message -> ChatMessageRes.builder()
                        .messageId(message.getId())
                        .sender(message.getMember().getNickname())
                        .content(message.getContent())
                        .sendDate(message.getSendDate())
                        .isRead(message.getId() <= finalLastReadId)  // 읽음 여부 설정
                        .build())
                .collect(Collectors.toList());
    }

//    public List<ChatRoomRes> toChatRoomList(List<UserChatRoom> chatRooms) {
//        return chatRooms.stream().map(this::toChatRoomDto).collect(Collectors.toList());
//    }
}

