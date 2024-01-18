package com.studymate.backend.member.dto;

import com.studymate.backend.file.domain.ProfileImg;
import com.studymate.backend.file.dto.ProfileImgResponse;
import com.studymate.backend.member.domain.Authority;
import com.studymate.backend.member.domain.Interests;
import com.studymate.backend.member.domain.Part;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.Set;

@AllArgsConstructor
@Builder
@Getter
public class MemberResponse {
    private String name;
    private String nickname;
    private Part part;
    private String email;
    private Interests interests;
    private String imageUrl;
    private Set<Authority> authoritiesDtoSet;
}
