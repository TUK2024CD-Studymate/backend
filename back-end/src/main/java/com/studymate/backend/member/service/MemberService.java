package com.studymate.backend.member.service;

import com.studymate.backend.commons.firebase.FCMTokenManager;
import com.studymate.backend.config.security.SecurityUtil;
import com.studymate.backend.config.security.jwt.TokenProvider;
import com.studymate.backend.member.MemberMapper;
import com.studymate.backend.member.MemberRepository;
import com.studymate.backend.member.domain.Interests;
import com.studymate.backend.member.domain.Member;
import com.studymate.backend.member.domain.Part;
import com.studymate.backend.member.dto.*;
import com.studymate.backend.member.exception.DuplicateMemberException;
import com.studymate.backend.member.exception.NotFoundMemberException;
import com.studymate.backend.review.domain.Review;
import com.studymate.backend.review.dto.ReviewUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.TimeUnit;


@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final MemberMapper memberMapper;
    private final RedisTemplate<String, String> redisTemplate;
    private final TokenProvider tokenProvider;

    @Transactional

    public String signup(MemberRequest request) {
        if (memberRepository.findOneWithAuthoritiesByEmail(request.getEmail()).orElse(null) != null) {
            throw new DuplicateMemberException("이미 가입되어 있는 회원입니다.");
        }

        Member member = memberMapper.toEntity(request);
        memberRepository.save(member);

        return "회원가입이 완료되었습니다.";
    }

    @Transactional
    public void logout(TokenRequestDto tokenRequestDto) {
        if (!tokenProvider.validateToken(tokenRequestDto.getAccessToken())) {
            throw new IllegalArgumentException("로그아웃 : 유효하지 않은 토큰입니다.");
        }

        Authentication authentication = tokenProvider.getAuthentication(tokenRequestDto.getAccessToken());

        if (redisTemplate.opsForValue().get(authentication.getName()) != null) {
            redisTemplate.delete(authentication.getName());
        }


        Long expiration = tokenProvider.getExpiration(tokenRequestDto.getAccessToken());
        redisTemplate.opsForValue().set(tokenRequestDto.getAccessToken(), "logout", expiration, TimeUnit.MILLISECONDS);
    }

    @Transactional(readOnly = true)
    public MemberResponse getMyMemberWithAuthorities() {
        Member member = getMember();
        return memberMapper.toResponse(member);
    }

    @Transactional
    public MemberResponse update(MemberUpdateRequest request) {

        Member member = SecurityUtil.getCurrentUsername()
                .flatMap(memberRepository::findOneWithAuthoritiesByEmail)
                .orElseThrow(() -> new NotFoundMemberException("Member not found"));

        member.update(request);

        return memberMapper.toResponse(member);
    }

    @Transactional
    public String delete() {

        Member member = SecurityUtil.getCurrentUsername()
                .flatMap(memberRepository::findOneWithAuthoritiesByEmail)
                .orElseThrow(() -> new NotFoundMemberException("Member not found"));

        memberRepository.delete(member);
        return "정상적으로 탈퇴되었습니다.";
    }

    public Member getMember() {
        Member member = SecurityUtil.getCurrentUsername().flatMap(memberRepository::findOneWithAuthoritiesByEmail)
                .orElseThrow(() -> new RuntimeException("Member not found"));

        return member;
    }

    public MemberListResponse findMentorByInterest(Interests interests) {
        List<Member> memberList = memberRepository.findAllByInterestsAndPart(interests, Part.MENTOR);
        return memberMapper.toListResponse(memberList);
    }

    @Transactional
    public void setMentorInfoByReview(Review review, Member mentor) {

        if (review.getIsSolved()) {
            mentor.updateSolved();
        }
        if (review.getHeart()) {
            mentor.updateHeart();
        }
        mentor.updateReviewCount();
        mentor.updateMatchingCount();
        mentor.setStarNum(review.getStar());
        mentor.setStarAverage(mentor.getReviewCount());
    }

    @Transactional
    public void updateMentorInfoByReview(Review review, Member mentor, ReviewUpdateRequest request) {
        if (review.getIsSolved() && !request.getIsSolved()) {
            mentor.subSolved();
        }
        if (!review.getIsSolved() && request.getIsSolved()) {
            mentor.updateSolved();
        }
        if (review.getHeart() && !request.getHeart()) {
            mentor.subHeart();
        }
        if (!review.getHeart() && request.getHeart()) {
            mentor.updateHeart();
        }
        mentor.updateStarNum(review.getStar(), request.getStar());
        mentor.setStarAverage(mentor.getReviewCount());
    }
}