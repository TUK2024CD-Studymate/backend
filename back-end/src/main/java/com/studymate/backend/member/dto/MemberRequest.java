package com.studymate.backend.member.dto;

import com.studymate.backend.member.domain.Authority;
import com.studymate.backend.member.domain.Interests;
import com.studymate.backend.member.domain.Member;
import com.studymate.backend.member.domain.Part;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Set;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MemberRequest {
    @Email
    @NotBlank
    private String email;
    @NotBlank
    private String password;
    @NotBlank
    private String name;
    @NotNull
    private Part part;
    @NotNull
    private Interests interests;
    @NotBlank
    private String nickname;
    private String profileUrl;
    private Set<Authority> authoritiesDtoSet;
}
