package com.studymate.backend.notification.repository;

import com.studymate.backend.member.domain.Member;
import com.studymate.backend.notification.domain.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findAllByMember(Member member);

    void deleteAllByMember(Member member);
}
