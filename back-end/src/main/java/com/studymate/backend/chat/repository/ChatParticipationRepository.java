package com.studymate.backend.chat.repository;

import com.studymate.backend.chat.entity.ChatParticipation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatParticipationRepository extends JpaRepository<ChatParticipation, Long> {
    boolean existsByMemberId(Long memberId);
    List<ChatParticipation> findByMemberId(Long memberId);

    void deleteByChattingRoomId(Long chattingRoomId);
}
