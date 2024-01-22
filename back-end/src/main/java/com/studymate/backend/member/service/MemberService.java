package com.studymate.backend.member.service;

import com.studymate.backend.config.security.SecurityUtil;
import com.studymate.backend.file.ProfileImgRepository;
import com.studymate.backend.file.domain.ProfileImg;
import com.studymate.backend.member.MemberMapper;
import com.studymate.backend.member.MemberRepository;
import com.studymate.backend.member.domain.Member;
import com.studymate.backend.member.dto.MemberRequest;
import com.studymate.backend.member.dto.MemberResponse;
import com.studymate.backend.member.dto.MemberUpdateRequest;
import com.studymate.backend.member.exception.DuplicateMemberException;
import com.studymate.backend.member.exception.NotFoundMemberException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final MemberMapper memberMapper;
    private final ProfileImgRepository profileImgRepository;

    @Transactional
    public MemberResponse signup(MemberRequest request) {
        if (memberRepository.findOneWithAuthoritiesByEmail(request.getEmail()).orElse(null) != null) {
            throw new DuplicateMemberException("이미 가입되어 있는 회원입니다.");
        }

        Member member = memberMapper.toEntity(request);

        ProfileImg image = ProfileImg.builder()
                .name("프로필 사진이 없습니다.")
                .member(member)
                .build();

        profileImgRepository.save(image);

        return memberMapper.toResponse(memberRepository.save(member));
    }

    @Transactional(readOnly = true)
    public MemberResponse getMyMemberWithAuthorities() {
        return memberMapper.toResponse(
                SecurityUtil.getCurrentUsername()
                        .flatMap(memberRepository::findOneWithAuthoritiesByEmail)
                        .orElseThrow(() -> new NotFoundMemberException("Member not found"))
        );
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
}
