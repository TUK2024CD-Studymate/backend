package com.studymate.backend.review;

import com.studymate.backend.file.ProfileImgRepository;
import com.studymate.backend.file.domain.ProfileImg;
import com.studymate.backend.member.domain.Member;
import com.studymate.backend.review.domain.Review;
import com.studymate.backend.review.dto.ReviewCreateRequest;
import com.studymate.backend.review.dto.ReviewResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;


@Component
@RequiredArgsConstructor
public class ReviewMapper {
    private final ProfileImgRepository profileImgRepository;

    public Review toEntity(ReviewCreateRequest request, Member member) {
        return Review.builder()
                .star(request.getStar())
                .title(request.getTitle())
                .member(member)
                .content(request.getContent())
                .build();
    }

    public ReviewResponse toResponse(Review review) {
        String imageUrl = "프로필 사진이 없습니다.";
        Member member = review.getMember();
        if (profileImgRepository.findByMember(member).isPresent()) {
            Optional<ProfileImg> profileImg = profileImgRepository.findByMember(member);
            imageUrl = profileImg.get().getUrl();
        }

        return ReviewResponse.builder()
                .isSolved(review.getIsSolved())
                .reviewId(review.getId())
                .writer(review.getWriter())
                .mentor(review.getMentor())
                .content(review.getContent())
                .imageUrl(imageUrl)
                .title(review.getTitle())
                .heart(review.getHeart())
                .star(review.getStar())
                .createAt(review.getCreatedAt())
                .build();
    }
}
