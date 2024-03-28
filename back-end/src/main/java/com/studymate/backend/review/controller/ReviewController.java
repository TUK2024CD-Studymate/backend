package com.studymate.backend.review.controller;

import com.studymate.backend.review.dto.ReviewCreateRequest;
import com.studymate.backend.review.dto.ReviewResponse;
import com.studymate.backend.review.dto.ReviewUpdateRequest;
import com.studymate.backend.review.service.ReviewService;
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
@Tag(name = "리뷰", description = "리뷰 API")
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping("/review/{mentorId}")
    @Operation(summary = "리뷰 작성", description = "사용자가 매칭 및 문제해결을 마친 후 리뷰를 작성한다")
    @ApiResponses(value = @ApiResponse(responseCode = "201", description = "성공"))
    public ResponseEntity<ReviewResponse> create(@PathVariable(value = "mentorId")Long id,
                                                 @RequestBody ReviewCreateRequest request) {
        return ResponseEntity.ok().body(reviewService.create(request, id));
    }

    @GetMapping("/review/{reviewId}")
    @Operation(summary = "리뷰 상세조회", description = "사용자가 작성한 리뷰를 상세조회한다.")
    @ApiResponses(value = @ApiResponse(responseCode = "201", description = "성공"))
    public ResponseEntity<ReviewResponse> getReview(@PathVariable(value = "reviewId")Long id) {
        return ResponseEntity.ok().body(reviewService.getReview(id));
    }

    @GetMapping("/reviews")
    @Operation(summary = "리뷰 전체조회", description = "사용자가 작성한 리뷰를 전체조회한다.")
    @ApiResponses(value = @ApiResponse(responseCode = "201", description = "성공"))
    public ResponseEntity<List<ReviewResponse>> getAllMyReviews() {
        return ResponseEntity.ok().body(reviewService.getAllReviews());
    }

    @PutMapping("/review/{reviewId}")
    @Operation(summary = "리뷰 수정", description = "사용자가 작성한 리뷰를 수정한다.")
    @ApiResponses(value = @ApiResponse(responseCode = "201", description = "성공"))
    public ResponseEntity<ReviewResponse> updateReview(@PathVariable(value = "reviewId") Long id,
                                                       @RequestBody ReviewUpdateRequest request) {
        return ResponseEntity.ok().body(reviewService.update(id, request));
    }

    @DeleteMapping("/review/{reviewId}")
    @Operation(summary = "리뷰 삭제", description = "사용자가 작성한 리뷰를 삭제한다.")
    @ApiResponses(value = @ApiResponse(responseCode = "201", description = "성공"))
    public ResponseEntity<String> delete(@PathVariable(value = "reviewId") Long id) {
        return ResponseEntity.ok().body(reviewService.delete(id));
    }
}
