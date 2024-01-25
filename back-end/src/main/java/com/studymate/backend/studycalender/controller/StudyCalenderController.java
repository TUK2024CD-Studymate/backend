package com.studymate.backend.studycalender.controller;

import com.studymate.backend.studycalender.dto.CalenderCreateRequest;
import com.studymate.backend.studycalender.service.StudyCalenderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class StudyCalenderController {

    private final StudyCalenderService studyCalenderService;

    @PostMapping("/calender")
    @Operation(summary = "create post", description = "회원이 스터디 기록을 생성한다.")
    @ApiResponses(value = @ApiResponse(responseCode = "200", description = "성공"))
    public ResponseEntity<String> createPost(@Valid @RequestBody CalenderCreateRequest request) {
        return ResponseEntity.ok(studyCalenderService.createPost(request));
    }
}
