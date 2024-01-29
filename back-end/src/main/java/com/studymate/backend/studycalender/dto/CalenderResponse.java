package com.studymate.backend.studycalender.dto;

import com.studymate.backend.member.domain.Interests;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
@Builder
public class CalenderResponse {

    private String content;

    private Interests studyClass;

    private LocalDateTime startTime;

    private LocalDateTime endTime;
}
