package com.studymate.backend.question.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.studymate.backend.member.domain.Interests;
import com.studymate.backend.question.domain.Question;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class QuestionResponse {
    private Long id;
    private String writer;
    private String title;
    private String content;
    private Interests interests;
    private Boolean isSolved;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createAt;

    public QuestionResponse toResponse(Question question) {
        return QuestionResponse.builder()
                .id(question.getId())
                .writer(question.getWriter())
                .title(question.getTitle())
                .content(question.getContent())
                .interests(question.getInterests())
                .createAt(question.getCreatedAt())
                .isSolved(question.getIsSolved())
                .build();
    }

    public List<QuestionResponse> toList(List<Question> questionList) {
        return questionList.stream().map(this::toResponse).collect(Collectors.toList());
    }
}
