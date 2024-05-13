package com.studymate.backend.chat.service;

import com.studymate.backend.chat.domain.ChatMessage;
import com.studymate.backend.chat.domain.ChatRoom;
import com.studymate.backend.chat.domain.UserChatRoom;
import com.studymate.backend.chat.dto.ChatMessageRes;
import com.studymate.backend.chat.dto.ChatRoomRes;
import com.studymate.backend.chat.dto.CreateMessageReq;
import com.studymate.backend.chat.mapper.ChatMapper;
import com.studymate.backend.chat.repository.ChatMessageRepository;
import com.studymate.backend.chat.repository.ChatRoomRepository;
import com.studymate.backend.chat.repository.UserChatRoomRepository;
import com.studymate.backend.member.MemberRepository;
import com.studymate.backend.member.domain.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatService {

    private final UserChatRoomRepository userChatRoomRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final ChatMapper chatMapper;
    private final MemberRepository memberRepository;
    private final MessageService messageService;




    @Transactional
    public ChatRoom createChatRoom(String chatRoomName) {
        ChatRoom newChatRoom = ChatRoom.builder()
                .name(chatRoomName) // 이름 설정
                .build();
        chatRoomRepository.save(newChatRoom);

        return newChatRoom;
    }

    @Transactional
    public void createUserChatRoom(Member member, Long chatRoomId) {
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new IllegalArgumentException("채팅룸 아이디에 해당하는 채팅룸이 존재하지 않습니다: " + chatRoomId));
        UserChatRoom newUserChatRoom = chatMapper.toUserChatRoom(member, chatRoom);

        userChatRoomRepository.save(newUserChatRoom);
    }

    @Transactional
    public void addUserToRoom(Long roomId, Long memberId) {
        UserChatRoom userChatRoom = new UserChatRoom();
        userChatRoom.setChatRoom(chatRoomRepository.findById(roomId).orElseThrow(() -> new RuntimeException("Room not found")));
        userChatRoom.setMember(memberRepository.findById(memberId).orElseThrow(() -> new RuntimeException("Member not found")));
        userChatRoomRepository.save(userChatRoom);
    }

    public boolean duplicatedUserChatRoom(Member member) {
        return userChatRoomRepository.existsByMemberId(member.getId());
    }


    @Transactional
    public List<ChatRoomRes> findUserChatRoomByMemberId(Long memberId) {
        List<UserChatRoom> userChatRooms = userChatRoomRepository.findByMemberId(memberId);
        List<ChatRoomRes> chatRoomResList = new ArrayList<>();

        for (UserChatRoom userChatRoom : userChatRooms) {
            ChatRoom chatRoom = userChatRoom.getChatRoom();
            // 모든 멤버 조회
            List<Member> allMembers = userChatRoomRepository.findByChatRoomId(chatRoom.getId())
                    .stream()
                    .map(UserChatRoom::getMember)
                    .collect(Collectors.toList());
            // 현재 사용자를 제외한 멤버 필터링
            List<Member> otherMembers = allMembers.stream()
                    .filter(m -> !m.getId().equals(memberId))
                    .collect(Collectors.toList());
            Long unreadMessageCount = countUnreadMessages(chatRoom.getId(), memberId); // 읽지 않은 메시지 수 계산

            ChatRoomRes chatRoomRes = chatMapper.toChatRoomDto(chatRoom, otherMembers, unreadMessageCount); // 수정된 메소드 호출
            chatRoomResList.add(chatRoomRes);
        }
        return chatRoomResList;
    }


    @Transactional
    public void saveMessage(Member sender, Long chatRoomId, CreateMessageReq createMessageReq) {
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new IllegalArgumentException("채팅룸 아이디에 해당하는 채팅룸이 존재하지 않습니다: " + chatRoomId));
        ChatMessage message = chatMapper.toChatMessage(sender, chatRoom, createMessageReq);

        chatMessageRepository.save(message);
        log.info("메시지가 저장됐습니다.");
    }

    public Long countUnreadMessages(Long chatRoomId, Long memberId) {
        List<ChatMessage> chatMessages = chatMessageRepository.findByChatRoomId(chatRoomId);
        String lastReadMessageId = messageService.getUserLastReadPosition(chatRoomId.toString(), memberId.toString());
        long lastReadId = lastReadMessageId != null ? Long.parseLong(lastReadMessageId) : 0;

        return chatMessages.stream()
                .filter(msg -> msg.getId() > lastReadId && !msg.getMember().getId().equals(memberId))
                .count();
    }


    @Transactional
    public List<ChatMessageRes> findChatMessage(Long chatRoomId, Long memberId) {
        List<ChatMessage> chatMessages = chatMessageRepository.findByChatRoomId(chatRoomId);
        messageService.updateUserReadPosition(chatRoomId.toString(), memberId.toString(), getLastMessageId(chatMessages));
        // 다시 메시지를 로드하거나, 업데이트가 반영되기를 기다린 후 메시지 목록을 반환
        chatMessages = chatMessageRepository.findByChatRoomId(chatRoomId);
        String lastReadMessageId = messageService.getUserLastReadPosition(chatRoomId.toString(), memberId.toString());
        return chatMapper.toChatMessageList(chatMessages, lastReadMessageId);
    }

    private String getLastMessageId(List<ChatMessage> messages) {
        if (!messages.isEmpty()) {
            return messages.get(messages.size() - 1).getId().toString();
        }
        return "";
    }

}
