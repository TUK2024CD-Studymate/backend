package com.studymate.backend.review;

import com.studymate.backend.member.domain.Member;
import com.studymate.backend.review.domain.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    List<Review> findAllByMember(Member member);

    List<Review> findAllByMentor(String mentor);
}
