package com.studymate.backend.studycalender.dto;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.studymate.backend.member.domain.Interests;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "Member Create Calender request")
public class CalenderCreateRequest {

    @NotBlank
    @Schema(description = "스터디 내용", nullable = false, example = "일차함수란 무엇인가? 수학에서 일차 함수(一次函數, 영어: linear function)는 최고 차수가 1 이하인 다항 함수이다. 즉, 그래프가 직선인 함수이다. 정비례 함수(正比例函數 영어: directly proportional function)는 일차 함수에 상수항이 0이라는 조건을 추가한 특수한 경우이다. 즉, 그래프가 원점을 지나는 직선인 함수이다. 단, 계수는 실수여야 한다.")
    private String content;

    @NotNull
    @Schema(description = "스터디 분류", nullable = false, example = "MATH")
    private Interests studyClass;

    @NotNull
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm", timezone = "Asia/Seoul")
    @Schema(description = "스터디 시작 시간", nullable = false, example = "2024-02-13 13:50")
    private LocalDateTime startTime;

    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm", timezone = "Asia/Seoul")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @Schema(description = "스터디 종료 시간", nullable = false, example = "2024-02-13 15:50")
    private LocalDateTime endTime;
}
