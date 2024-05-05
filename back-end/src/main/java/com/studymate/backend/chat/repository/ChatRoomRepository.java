package com.studymate.backend.chat.repository;

import com.studymate.backend.chat.domain.ChatRoom;
import jakarta.annotation.PostConstruct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.*;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {

}