package com.studymate.backend.heart;

import com.studymate.backend.heart.domain.Heart;
import com.studymate.backend.member.domain.Member;
import com.studymate.backend.post.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface HeartRepository extends JpaRepository<Heart, Long> {
    Optional<Heart> findByMemberAndPost(Member member, Post post);
}
