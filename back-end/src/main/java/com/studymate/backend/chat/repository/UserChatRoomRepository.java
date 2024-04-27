package com.studymate.backend.chat.repository;

import com.studymate.backend.chat.domain.UserChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserChatRoomRepository extends JpaRepository<UserChatRoom, Long> {
    boolean existsByMemberId(Long memberId);
    Optional<UserChatRoom> findByMemberId(Long memberId);
}