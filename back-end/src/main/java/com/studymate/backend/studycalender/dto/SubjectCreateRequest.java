package com.studymate.backend.studycalender.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class SubjectCreateRequest {
    @NotNull
    @Schema(description = "과목 제목", nullable = false, example = "알고리즘")
    private String subjectName;
}
