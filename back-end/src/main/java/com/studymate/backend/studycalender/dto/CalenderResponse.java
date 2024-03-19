package com.studymate.backend.studycalender.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.studymate.backend.member.domain.Interests;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
@Builder
public class CalenderResponse {
    private Long id;

    private String content;

    private Interests studyClass;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm", timezone = "Asia/Seoul")
    private LocalDateTime startTime;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm", timezone = "Asia/Seoul")
    private LocalDateTime endTime;

    private String entireTime;
}
