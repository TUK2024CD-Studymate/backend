package com.studymate.backend.member.service;

import com.studymate.backend.commons.firebase.FCMTokenManager;
import com.studymate.backend.config.security.SecurityUtil;
import com.studymate.backend.file.ProfileImgRepository;
import com.studymate.backend.file.domain.ProfileImg;
import com.studymate.backend.member.MemberMapper;
import com.studymate.backend.member.MemberRepository;
import com.studymate.backend.member.domain.Interests;
import com.studymate.backend.member.domain.Member;
import com.studymate.backend.member.domain.Part;
import com.studymate.backend.member.dto.*;
import com.studymate.backend.member.exception.DuplicateMemberException;
import com.studymate.backend.member.exception.NotFoundMemberException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final MemberMapper memberMapper;
    private final FCMTokenManager fcmTokenManager;

    @Transactional
    public String signup(MemberRequest request) {
        if (memberRepository.findOneWithAuthoritiesByEmail(request.getEmail()).orElse(null) != null) {
            throw new DuplicateMemberException("이미 가입되어 있는 회원입니다.");
        }

        Member member = memberMapper.toEntity(request);
        memberRepository.save(member);

        return "회원가입이 완료되었습니다.";
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

    public void fcmLogin(MemberLoginRequest request) {
        final String fcmToken = request.getFcmToken();
        Member member = memberRepository.findByEmail(request.getEmail());
        final Long memberId = member.getId();
        deleteAndSaveFCMToken(fcmToken, memberId);
    }

    /*
     * 기존에 존재하는 Fcm토큰을 삭제한다.
     * Redis에 사용자 아이디를 Key로 Fcm토큰을 저장한다.
     */
    private void deleteAndSaveFCMToken(String fcmToken, Long userId) {
        fcmTokenManager.deleteAndSaveFCMToken(String.valueOf(userId),fcmToken);
    }
}