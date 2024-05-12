package com.studymate.backend.studycalender.dto;

import lombok.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class SubjectResponse {
    private Long id;
    private String subjectName;
}
