package com.studymate.backend.member.service;

import com.studymate.backend.config.security.SecurityUtil;
import com.studymate.backend.file.ProfileImgRepository;
import com.studymate.backend.file.domain.ProfileImg;
import com.studymate.backend.member.MemberMapper;
import com.studymate.backend.member.MemberRepository;
import com.studymate.backend.member.domain.Interests;
import com.studymate.backend.member.domain.Member;
import com.studymate.backend.member.domain.Part;
import com.studymate.backend.member.dto.MemberListResponse;
import com.studymate.backend.member.dto.MemberRequest;
import com.studymate.backend.member.dto.MemberResponse;
import com.studymate.backend.member.dto.MemberUpdateRequest;
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
    private final ProfileImgRepository profileImgRepository;

    @Transactional
    public String signup(MemberRequest request) {
        if (memberRepository.findOneWithAuthoritiesByEmail(request.getEmail()).orElse(null) != null) {
            throw new DuplicateMemberException("이미 가입되어 있는 회원입니다.");
        }

        Member member = memberMapper.toEntity(request);

//        ProfileImg image = ProfileImg.builder()
//                .name("프로필 사진이 없습니다.")
//                .member(member)
//                .build();
//
//        profileImgRepository.save(image);
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
}