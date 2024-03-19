package com.studymate.backend.heart.controller;

import com.studymate.backend.heart.service.HeartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Tag(name = "게시물", description = "게시물 API")
public class HeartController {
    private final HeartService heartService;

    @PostMapping("/post/heart/{postId}")
    @Operation(summary = "좋아요", description = "회원이 좋아요를 누른다.")
    @ApiResponses(value = @ApiResponse(responseCode = "200", description = "성공"))
    public ResponseEntity<String> insert(@PathVariable("postId") Long id) throws Exception{
        return ResponseEntity.ok().body(heartService.insert(id));
    }

    @DeleteMapping("/post/heart/{postId}")
    @Operation(summary = "좋아요 취소", description = "회원이 좋아요를 취소한다.")
    @ApiResponses(value = @ApiResponse(responseCode = "200", description = "성공"))
    public ResponseEntity<String> delete(@PathVariable("postId") Long id) throws Exception{
        return ResponseEntity.ok().body(heartService.delete(id));
    }
}
