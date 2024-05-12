package com.studymate.backend.studycalender.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SubjectUpdateRequest {
    @NotNull
    @Schema(description = "과목 제목", nullable = false, example = "스프링")
    private String subjectName;
}
