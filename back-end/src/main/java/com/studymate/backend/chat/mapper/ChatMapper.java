package com.studymate.backend.chat.mapper;

import com.studymate.backend.chat.domain.ChatMessage;
import com.studymate.backend.chat.domain.ChatRoom;
import com.studymate.backend.chat.dto.ChatMessageRes;
import com.studymate.backend.chat.dto.ChatRoomRes;
import com.studymate.backend.chat.dto.CreateMessageReq;
import com.studymate.backend.file.ProfileImgRepository;
import com.studymate.backend.member.domain.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import com.studymate.backend.file.domain.ProfileImg;


import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static java.time.LocalDateTime.now;
import static com.studymate.backend.global.constant.DefaultProfileImg.DEFAULT_PROFILE_IMG;

@Slf4j
@Component
@RequiredArgsConstructor
public class ChatMapper {
    private final ProfileImgRepository profileImgRepository;

    public ChatMessage toChatMessage(Member member, ChatRoom chatRoom, CreateMessageReq request) {
        return ChatMessage.builder()
                .member(member)
                .chatRoom(chatRoom)
                .content(request.getContent())
                .sendDate(now())
                .build();
    }

    public ChatRoomRes toChatRoomDto(ChatRoom chatRoom, List<Member> otherMembers, Long unreadMessageCount) {
        return ChatRoomRes.builder()
                .chatRoomId(chatRoom.getId())
                .chatRoomName(chatRoom.getName())
                .members(otherMembers.stream()
                        .map(member -> {
                            String imageUrl = profileImgRepository.findByMember(member)
                                    .map(ProfileImg::getUrl)
                                    .orElse(DEFAULT_PROFILE_IMG);
                            return toMemberDetail(member, imageUrl);
                        })
                        .collect(Collectors.toList()))
                .unreadMessageCount(unreadMessageCount)
                .build();
    }

    private ChatRoomRes.MemberDetail toMemberDetail(Member member, String imageUrl) {
        return ChatRoomRes.MemberDetail.builder()
                .id(member.getId())
                .name(member.getName())
                .nickname(member.getNickname())
                .expertiseField(member.getExpertiseField())
                .interests(Collections.singletonList(member.getInterests().getKorean()))
                .profileImageUrl(imageUrl)
                .isLogin(member.isLogin())
                .build();
    }



    public List<ChatMessageRes> toChatMessageList(List<ChatMessage> messages, String lastReadMessageId) {
        long lastReadId = 0;
        if (lastReadMessageId != null && !lastReadMessageId.isEmpty()) {
            try {
                lastReadId = Long.parseLong(lastReadMessageId);
            } catch (NumberFormatException e) {
                log.error("Failed to parse lastReadMessageId: {}", lastReadMessageId, e);
                throw new IllegalArgumentException("Invalid lastReadMessageId: " + lastReadMessageId);
            }
        }

        long finalLastReadId = lastReadId;
        return messages.stream()
                .map(message -> {
                    String imageUrl = profileImgRepository.findByMember(message.getMember())
                            .map(ProfileImg::getUrl)
                            .orElse(DEFAULT_PROFILE_IMG);
                    return ChatMessageRes.builder()
                            .messageId(message.getId())
                            .sender(message.getMember().getNickname())
                            .content(message.getContent())
                            .sendDate(message.getSendDate())
                            .isRead(message.getId() <= finalLastReadId)
                            .profileImageUrl(imageUrl) // 프로필 이미지 URL 추가
                            .build();
                })
                .collect(Collectors.toList());
    }

}

