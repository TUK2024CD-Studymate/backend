package com.studymate.backend.chat.service;

import com.studymate.backend.chat.entity.ChatParticipation;
import com.studymate.backend.chat.entity.ChattingRoom;
import com.studymate.backend.chat.error.ChatParticipationNotFoundException;
import com.studymate.backend.chat.repository.ChatParticipationRepository;
import com.studymate.backend.chat.repository.ChattingRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ChatServiceValidator {
    private final ChatParticipationRepository chatParticipationRepository;
    private final ChattingRoomRepository chattingRoomRepository;

    public ChattingRoom validateChattingRoomExists(Long chattingRoomId) {
        return chattingRoomRepository.findById(chattingRoomId)
                .orElseThrow(() -> new IllegalArgumentException("채팅룸 아이디에 해당하는 채팅룸이 존재하지 않습니다: " + chattingRoomId));
    }

    public List<ChatParticipation> validateUserParticipation(Long memberId) {
        List<ChatParticipation> participations = chatParticipationRepository.findByMemberId(memberId);
        if (participations.isEmpty()) {
            throw new ChatParticipationNotFoundException("사용자 ID에 해당하는 채팅 참여내역을 찾을 수 없습니다: " + memberId);
        }
        return participations;
    }

    public boolean checkForDuplicatedParticipation(Long memberId) {
        return chatParticipationRepository.existsByMemberId(memberId);
    }

}
