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
import com.studymate.backend.member.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final UserChatRoomRepository userChatRoomRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final ChatMapper chatMapper;

    @Transactional
    public ChatRoom createChatRoom() {
        ChatRoom newChatRoom = ChatRoom.builder().build();
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

    public boolean duplicatedUserChatRoom(Member member) {
        return userChatRoomRepository.existsByMemberId(member.getId());
    }

    public UserChatRoom findUserChatRoomByMemberId(Long memberId) {
        return userChatRoomRepository.findByMemberId(memberId)
                .orElseThrow(() -> new IllegalArgumentException("채팅룸 아이디에 해당하는 채팅룸이 존재하지 않습니다")); // TODO: 에러처리 수정
    }

    public void saveMessage(Member sender, Long chatRoomId, CreateMessageReq createMessageReq) {
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new IllegalArgumentException("채팅룸 아이디에 해당하는 채팅룸이 존재하지 않습니다: " + chatRoomId));
        ChatMessage message = chatMapper.toChatMessage(sender, chatRoom, createMessageReq);

        chatMessageRepository.save(message);
    }

    public List<ChatMessageRes> findChatMessage(Long chatRoomId) {
        List<ChatMessage> chatMessages = chatMessageRepository.findByChatRoomId(chatRoomId);

        return chatMapper.toChatMessageList(chatMessages);
    }

    public List<ChatRoomRes> findChatRoom() {
        List<UserChatRoom> chatRooms = userChatRoomRepository.findAll();

        return chatMapper.toChatRoomList(chatRooms);
    }
}
