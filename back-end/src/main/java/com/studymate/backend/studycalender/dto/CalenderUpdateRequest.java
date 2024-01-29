package com.studymate.backend.studycalender.dto;

import com.studymate.backend.member.domain.Interests;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@Builder
@NoArgsConstructor
@Schema(description = "Member Update Calender request")
public class CalenderUpdateRequest {

    @NotBlank
    @Schema(description = "스터디 내용", nullable = false, example = "조동사(助動詞, auxiliary verb (aux))란 동사와 같은 형태를 가지지만, 다른 동사와 이어져서 상, 법 등의 문법 기능을 나타내는 단어이다. 일본어의 ‘-ている’나 ‘-ておく’ 등, 영어의 can이나 will 등이 있다.")
    private String content;

    @NotNull
    @Schema(description = "스터디 분류", nullable = false, example = "ENGLISH")
    private Interests interests;

    @NotNull
    @Schema(description = "스터디 시작 시간", nullable = false, example = "2024-01-25T18:30:49.224Z")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime startTime;

    @Schema(description = "스터디 시작 시간", nullable = false, example = "2024-01-25T20:30:49.224Z")
    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime endTime;
}
