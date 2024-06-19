package com.studymate.backend.review;

import com.studymate.backend.file.ProfileImgRepository;
import com.studymate.backend.file.domain.ProfileImg;
import com.studymate.backend.member.domain.Member;
import com.studymate.backend.review.domain.Review;
import com.studymate.backend.review.dto.ReviewCreateRequest;
import com.studymate.backend.review.dto.ReviewResponse;
import com.studymate.backend.review.dto.ReviewResponseList;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


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
        String imageUrl = "https://studymate-bucket.s3.ap-northeast-2.amazonaws.com/profileImg.png";
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

    public ReviewResponseList toListResponse(List<Review> reviewList) {
        List<ReviewResponse> reviewResponses = reviewList.stream()
                .map(this::toResponse).collect(Collectors.toList());

        return ReviewResponseList.builder()
                .reviewResponses(reviewResponses)
                .reviewCount(reviewList.size())
                .build();
    }
}
