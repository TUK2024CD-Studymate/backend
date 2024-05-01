package com.studymate.backend.matching.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MatchingSseResponse {
    private String nickname;
    private Long question_id;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm", timezone = "Asia/Seoul")
    private String likedTime;
}
