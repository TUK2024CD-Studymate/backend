package com.studymate.backend.member;

import com.studymate.backend.file.ProfileImgRepository;
import com.studymate.backend.file.domain.ProfileImg;
import com.studymate.backend.member.domain.Authority;
import com.studymate.backend.member.domain.Member;
import com.studymate.backend.member.dto.MemberListResponse;
import com.studymate.backend.member.dto.MemberRequest;
import com.studymate.backend.member.dto.MemberResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class MemberMapper {

    private final PasswordEncoder passwordEncoder;
    private final ProfileImgRepository profileImgRepository;

    public Member toEntity(MemberRequest request) {

        Authority authority = Authority.builder().
                authorityName("ROLE_USER")
                .build();

        Member member = Member.builder()
                .nickname(request.getNickname())
                .name(request.getName())
                .email(request.getEmail())
                .part(request.getPart())
                .interests(request.getInterests())
                .password(passwordEncoder.encode(request.getPassword()))
                .activated(true)
                .isDeleted(false)
                .authorities(Collections.singleton(authority))
                .build();

        return member;
    }

    public MemberResponse toResponse(Member member) {
        if (member == null) return null;

        ProfileImg image = profileImgRepository.findByMember(member);
        String imageName = image.getName();

        return MemberResponse.builder()
                .email(member.getEmail())
                .part(member.getPart())
                .nickname(member.getNickname())
                .interests(member.getInterests())
                .imageUrl(imageName)
                .name(member.getName())
                .build();
    }

    public MemberListResponse toListResponse(List<Member> memberList) {
        List<MemberResponse> memberResponseList =
            memberList.stream().map(this::toResponse).collect(Collectors.toList());
        return MemberListResponse.builder()
                .memberList(memberResponseList)
                .build();
    }
}
