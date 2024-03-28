package com.studymate.backend.review;

import com.studymate.backend.member.domain.Member;
import com.studymate.backend.review.domain.Review;
import com.studymate.backend.review.dto.ReviewCreateRequest;
import com.studymate.backend.review.dto.ReviewResponse;
import org.springframework.stereotype.Component;

@Component
public class ReviewMapper {

    public Review toEntity(ReviewCreateRequest request, Member member) {

        return Review.builder()
                .star(request.getStar())
                .title(request.getTitle())
                .member(member)
                .content(request.getContent())
                .build();
    }

    public ReviewResponse toResponse(Review review) {
        return ReviewResponse.builder()
                .isSolved(review.getIsSolved())
                .reviewId(review.getId())
                .writer(review.getWriter())
                .mentor(review.getMentor())
                .content(review.getContent())
                .title(review.getTitle())
                .heart(review.getHeart())
                .star(review.getStar())
                .createAt(review.getCreatedAt())
                .build();
    }
}
