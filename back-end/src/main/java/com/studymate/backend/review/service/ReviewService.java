package com.studymate.backend.review.service;

import com.studymate.backend.member.MemberRepository;
import com.studymate.backend.member.domain.Member;
import com.studymate.backend.member.service.MemberService;
import com.studymate.backend.review.ReviewMapper;
import com.studymate.backend.review.ReviewRepository;
import com.studymate.backend.review.domain.Review;
import com.studymate.backend.review.dto.ReviewCreateRequest;
import com.studymate.backend.review.dto.ReviewResponse;
import com.studymate.backend.review.dto.ReviewUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final MemberRepository memberRepository;
    private final MemberService memberService;
    private final ReviewMapper reviewMapper;

    @Transactional
    public ReviewResponse create(ReviewCreateRequest request, Long id) {
        Member member = memberService.getMember();
        Member mentor = memberRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("not found mentor id"));

        Review review = reviewMapper.toEntity(request, member);

        review.confirmWriter(member);
        review.confirmMentor(mentor);
        review.setSolved(request.getIsSolved());
        review.setHeart(request.getHeart());

        memberService.setMentorInfoByReview(review, mentor);

        reviewRepository.save(review);

        return reviewMapper.toResponse(review);
    }

    public ReviewResponse getReview(Long id) {
        Review review = reviewRepository.findById(id).orElseThrow(() -> new RuntimeException("not found review id"));

        return reviewMapper.toResponse(review);
    }


    public List<ReviewResponse> getAllReviews() {
        Member member = memberService.getMember();

        List<Review> reviews = reviewRepository.findAllByMember(member);

        List<ReviewResponse> reviewResponses = reviews.stream().map(reviewMapper::toResponse)
                .toList();

        return reviewResponses;
    }

    @Transactional
    public ReviewResponse update(Long id, ReviewUpdateRequest request) {
        Review review = reviewRepository.findById(id).orElseThrow(() -> new RuntimeException("not found review id"));

        Member mentor = memberRepository.findByNickname(review.getMentor());

        memberService.updateMentorInfoByReview(review, mentor, request);

        review.update(request);

        return reviewMapper.toResponse(review);
    }

    public String delete(Long id) {
        Review review = reviewRepository.findById(id).orElseThrow(() -> new RuntimeException("not found review id"));

        reviewRepository.delete(review);
        return "삭제되었습니다.";
    }
}
