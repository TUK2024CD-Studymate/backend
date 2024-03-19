package com.studymate.backend.question.dto;

import com.studymate.backend.member.domain.Interests;
import com.studymate.backend.question.domain.Question;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class QuestionCreateRequest {

    @NotBlank
    @Schema(description = "질문 제목", nullable = false, example = "도와주세요ㅠ 연동이 안됩니다.")
    private String title;
    @NotBlank
    @Schema(description = "질문 내용", nullable = false, example = "mysql이랑 스프링 연동이 안됩니다..")
    private String content;
    @NotNull
    @Schema(description = "질문 주제", nullable = false, example = "PROGRAMMING")
    private Interests interests;

    public Question toEntity(QuestionCreateRequest request) {
        return Question.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .interests(request.getInterests())
                .isSolved(false)
                .build();
    }
}
