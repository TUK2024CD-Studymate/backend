package com.studymate.backend.question.controller;

import com.studymate.backend.question.dto.QuestionCreateRequest;
import com.studymate.backend.question.dto.QuestionResponse;
import com.studymate.backend.question.service.QuestionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Tag(name = "질문", description = "질문 API")
public class QuestionController {

    private final QuestionService questionService;

    @PostMapping("/question")
    @Operation(summary = "질문 작성", description = "사용자가 질문을 작성한다")
    @ApiResponses(value = @ApiResponse(responseCode = "201", description = "성공"))
    public ResponseEntity<QuestionResponse> create(@RequestBody QuestionCreateRequest request) {
        return ResponseEntity.ok().body(questionService.create(request));
    }

    @GetMapping("/question/{questionId}")
    @Operation(summary = "질문 조회", description = "특정 질문을 불러온다")
    @ApiResponses(value = @ApiResponse(responseCode = "201", description = "성공"))
    public ResponseEntity<QuestionResponse> getQuestion(@PathVariable("questionId") Long id) {
        return ResponseEntity.ok().body(questionService.getQuestion(id));
    }

    @GetMapping("/question")
    @Operation(summary = "질문 전체 조회", description = "회원별로 질문한 리스트를 불러온다")
    @ApiResponses(value = @ApiResponse(responseCode = "201", description = "성공"))
    public ResponseEntity<List<QuestionResponse>> getQuestions() {
        return ResponseEntity.ok().body(questionService.getQuestions());
    }
    //TODO 해결완료 유무로 질문 리스트 조회하는 api 설계?
}
