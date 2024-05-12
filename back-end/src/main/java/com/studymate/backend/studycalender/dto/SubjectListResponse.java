package com.studymate.backend.studycalender.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
@AllArgsConstructor
public class SubjectListResponse {
    List<SubjectResponse> subjectList;
}
