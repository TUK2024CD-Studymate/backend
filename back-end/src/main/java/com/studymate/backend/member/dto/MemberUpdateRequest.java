package com.studymate.backend.member.dto;


import com.studymate.backend.member.domain.Interests;
import com.studymate.backend.member.domain.Part;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MemberUpdateRequest {
    @NotBlank
    private String name;
    @NotBlank
    private Part part;
    @NotBlank
    private Interests interests;
    @NotBlank
    private String nickname;
}
