package com.studymate.backend.chat.service;

import com.studymate.backend.chat.dto.request.CreateMessageRequest;
import com.studymate.backend.chat.dto.response.ChatMessageResponse;
import com.studymate.backend.chat.dto.response.ChattingRoomResponse;
import com.studymate.backend.chat.entity.ChatMessage;
import com.studymate.backend.chat.entity.ChatParticipation;
import com.studymate.backend.chat.entity.ChattingRoom;
import com.studymate.backend.chat.mapper.ChatMapper;
import com.studymate.backend.chat.repository.ChatMessageRepository;
import com.studymate.backend.chat.repository.ChatParticipationRepository;
import com.studymate.backend.chat.repository.ChattingRoomRepository;
import com.studymate.backend.member.domain.Member;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChattingRoomService {
    private final ChattingRoomRepository chattingRoomRepository;
    private final ChatParticipationRepository chatParticipationRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final ChatMapper chatMapper;
    private final ChatServiceValidator chatServiceValidator;

    //채팅 생성
    @Transactional
    public ChattingRoom createChattingRoom(){
        ChattingRoom newChattingRoom = ChattingRoom.builder().build();
        chattingRoomRepository.save(newChattingRoom);

        return newChattingRoom;
    }

    // 채팅방 참여
    @Transactional
    public ChatParticipation createChatParticipation(Member member, Long chattingRoomId){
        ChattingRoom chattingRoom = chatServiceValidator.validateChattingRoomExists(chattingRoomId);

        ChatParticipation newChatParticipation = chatMapper.toChatParticipation(chattingRoom, member);
        return chatParticipationRepository.save(newChatParticipation);
    }

    // 채팅 참여 중복 여부
    public boolean duplicatedChatParticipation(Member member){
        return chatParticipationRepository.existsByMemberId(member.getId());
    }

    // userId 통해서 채팅 참여 내역 찾기
    public List<ChatParticipation> findChatParticipationByUserId(Long userId){
        return chatServiceValidator.validateUserParticipation(userId);
    }

    // 메시지 저장
    public void saveMessage(Member sender, Long chattingRoomId, CreateMessageRequest createMessageRequest){
        ChattingRoom chattingRoom = chatServiceValidator.validateChattingRoomExists(chattingRoomId);
        ChatMessage message = chatMapper.toChatMessage(sender, chattingRoom, createMessageRequest);
        chatMessageRepository.save(message);
    }

    // 채팅방 채팅 내역 응답
    public List<ChatMessageResponse> findChatMessage(Long chattingRoomId){
        List<ChatMessage> chatMessages = chatMessageRepository.findByChattingRoomId(chattingRoomId);
        return chatMapper.toChatMessageList(chatMessages);
    }

    // 사용자의 채팅방 내역
    public List<ChattingRoomResponse> findChattingRoom() {
        List<ChatParticipation> chattingRooms = chatParticipationRepository.findAll();
        return chatMapper.toChattingRoomList(chattingRooms);
    }

    // 채팅방 삭제
    @Transactional
    public String deleteChattingRoom(Long chattingRoomId) {
        ChattingRoom chattingRoom = chatServiceValidator.validateChattingRoomExists(chattingRoomId);

        // 채팅방에 관련된 메시지와 참여 내역을 먼저 삭제
        chatMessageRepository.deleteByChattingRoomId(chattingRoomId);
        chatParticipationRepository.deleteByChattingRoomId(chattingRoomId);

        // 채팅방 삭제
        chattingRoomRepository.delete(chattingRoom);
        return "해당 채팅방이 삭제되었습니다.";
    }
}
