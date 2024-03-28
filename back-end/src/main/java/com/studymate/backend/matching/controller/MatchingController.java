package com.studymate.backend.matching.controller;

import com.studymate.backend.matching.service.MatchingService;
import com.studymate.backend.member.dto.MemberListResponse;
import com.studymate.backend.review.dto.ReviewResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Tag(name = "매칭", description = "매칭 API")
public class MatchingController {

    private final MatchingService matchingService;

    @GetMapping("/matching/{questionId}")
    @Operation(summary = "멘토 조회", description = "질문 주제를 바탕으로 멘토의 정보들을 출력한다.")
    @ApiResponses(value = @ApiResponse(responseCode = "200", description = "성공"))
    public ResponseEntity<MemberListResponse> getMentorList(@PathVariable("questionId") Long questionId) {
        return ResponseEntity.ok().body(matchingService.getMentorList(questionId));
    }

    @GetMapping("/matching/{questionId}/{mentorId}")
    @Operation(summary = "멘토 매칭 알림", description = "해당 멘토에게 LMS알림을 전송한다.")
    @ApiResponses(value = @ApiResponse(responseCode = "200", description = "성공"))
    public ResponseEntity<String> matching(@PathVariable("questionId") Long questionId,
                                           @PathVariable("mentorId") Long mentorId) {
        return ResponseEntity.ok().body(matchingService.matchingForSms(questionId, mentorId));
    }

    @GetMapping("/matching/review/{mentorId}")
    @Operation(summary = "멘토의 리뷰들 조회", description = "해당 멘토에게 쓰여진 리뷰들을 불러온다.")
    @ApiResponses(value = @ApiResponse(responseCode = "200", description = "성공"))
    public ResponseEntity<List<ReviewResponse>> matching(@PathVariable("mentorId") Long mentorId) {
        return ResponseEntity.ok().body(matchingService.searchMentorReview(mentorId));
    }
}
