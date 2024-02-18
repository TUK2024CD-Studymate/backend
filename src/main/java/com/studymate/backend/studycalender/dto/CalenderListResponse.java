package com.studymate.backend.studycalender.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class CalenderListResponse {
    List<CalenderResponse> calenderList;
}
