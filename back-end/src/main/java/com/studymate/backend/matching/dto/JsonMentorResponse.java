package com.studymate.backend.matching.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class JsonMentorResponse {
    private Long id;
    private String publicRelations;
    private String expertiseField;

}
