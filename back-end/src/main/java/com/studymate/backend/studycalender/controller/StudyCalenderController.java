package com.studymate.backend.studycalender.controller;

import com.studymate.backend.studycalender.dto.*;
import com.studymate.backend.studycalender.service.CalenderSubjectService;
import com.studymate.backend.studycalender.service.StudyCalenderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Tag(name = "스터디기록", description = "스터디기록 API")
public class StudyCalenderController {

    private final StudyCalenderService studyCalenderService;
    private final CalenderSubjectService calenderSubjectService;

    @PostMapping("/subject")
    @Operation(summary = "과목 생성", description = "회원이 스터디 과목을 생성한다.")
    @ApiResponses(value = @ApiResponse(responseCode = "200", description = "성공"))
    public ResponseEntity<SubjectResponse> createPost(@Valid @RequestBody SubjectCreateRequest request) {
        return ResponseEntity.ok(calenderSubjectService.createSubject(request));
    }

    @PostMapping("/calender/{subject-id}")
    @Operation(summary = "스터디 기록 생성", description = "회원이 스터디 기록을 생성한다.")
    @ApiResponses(value = @ApiResponse(responseCode = "200", description = "성공"))
    public ResponseEntity<CalenderResponse> createPost(@Valid @RequestBody CalenderCreateRequest request,
                                                       @PathVariable("subject-id") Long id) {
        return ResponseEntity.ok(studyCalenderService.createCalender(request, id));
    }

    @GetMapping("/calender/{calender_id}")
    @Operation(summary = "스터디 기록 조회", description = "회원이 작성했던 스터디 기록을 조회한다.")
    @ApiResponses(value = @ApiResponse(responseCode = "200", description = "성공"))
    public ResponseEntity<CalenderResponse> findCalender(@PathVariable("calender_id") Long id) {
        return ResponseEntity.ok(studyCalenderService.findOne(id));
    }

    @PutMapping("/calender/{calender_id}")
    @Operation(summary = "스터디 기록 수정", description = "회원이 작성했던 스터디 기록을 수정한다.")
    @ApiResponses(value = @ApiResponse(responseCode = "200", description = "성공"))
    public ResponseEntity<CalenderResponse> updateCalender(@PathVariable("calender_id") Long id,
                                                           @Valid @RequestBody CalenderUpdateRequest request) {
        return ResponseEntity.ok(studyCalenderService.update(id, request));
    }

    @DeleteMapping("/calender/{calender_id}")
    @Operation(summary = "스터디 기록 삭제", description = "회원이 작성했던 스터디 기록을 삭제한다.")
    @ApiResponses(value = @ApiResponse(responseCode = "200", description = "성공"))
    public ResponseEntity<String> deleteCalender(@PathVariable("calender_id") Long id) {
        return ResponseEntity.ok(studyCalenderService.delete(id));
    }

    @DeleteMapping("/subject/{subject-id}")
    @Operation(summary = "과목 삭제", description = "회원이 생성한 과목을 삭제한다.")
    @ApiResponses(value = @ApiResponse(responseCode = "200", description = "성공"))
    public ResponseEntity<String> deleteCalender(@PathVariable("subject-id") Long id) {
        return ResponseEntity.ok(studyCalenderService.delete(id));
    }

    @GetMapping("/calender")
    @Operation(summary = "스터디 기록 전체조회", description = "회원이 작성한 스터디 기록을 전체 조회한다.")
    @ApiResponses(value = @ApiResponse(responseCode = "200", description = "성공"))
    public ResponseEntity<CalenderListResponse> findAllCalender() {
        return ResponseEntity.ok(studyCalenderService.findAll());
    }
}
