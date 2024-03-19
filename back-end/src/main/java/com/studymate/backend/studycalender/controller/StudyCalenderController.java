package com.studymate.backend.studycalender.controller;

import com.studymate.backend.studycalender.dto.CalenderCreateRequest;
import com.studymate.backend.studycalender.dto.CalenderListResponse;
import com.studymate.backend.studycalender.dto.CalenderResponse;
import com.studymate.backend.studycalender.dto.CalenderUpdateRequest;
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

    @PostMapping("/calender")
    @Operation(summary = "스터디 기록 생성", description = "회원이 스터디 기록을 생성한다.")
    @ApiResponses(value = @ApiResponse(responseCode = "200", description = "성공"))
    public ResponseEntity<Long> createPost(@Valid @RequestBody CalenderCreateRequest request) {
        return ResponseEntity.ok(studyCalenderService.createPost(request));
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

    @GetMapping("/calender")
    @Operation(summary = "스터디 기록 전체조회", description = "회원이 작성한 스터디 기록을 전체 조회한다.")
    @ApiResponses(value = @ApiResponse(responseCode = "200", description = "성공"))
    public ResponseEntity<CalenderListResponse> findAllCalender() {
        return ResponseEntity.ok(studyCalenderService.findAll());
    }
}
