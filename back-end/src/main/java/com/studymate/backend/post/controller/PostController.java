package com.studymate.backend.post.controller;

import com.studymate.backend.post.dto.PostRequestDto;
import com.studymate.backend.post.dto.PostResponseDto;
import com.studymate.backend.post.dto.PostUpdateRequestDto;
import com.studymate.backend.post.service.PostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@Tag(name = "게시물", description = "게시물 API")
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;

    @PostMapping("/posts")
    @Operation(summary = "게시글 생성", description = "게시글을 생성한다")
    @ApiResponses(value = @ApiResponse(responseCode = "201", description = "성공"))
    public ResponseEntity<PostResponseDto> createPost(@Valid @RequestBody PostRequestDto requestDto) {
        PostResponseDto postResponseDto = postService.save(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(postResponseDto);
    }

    @GetMapping("/posts")
    @Operation(summary = "전체 게시글 조회", description = "전체 게시글을 불러온다")
    @ApiResponses(value = @ApiResponse(responseCode = "200", description = "성공"))
    public ResponseEntity<List<PostResponseDto>> postList(){
        List<PostResponseDto> postResponseDtoList = postService.list();
        return ResponseEntity.ok(postResponseDtoList);
    }

    @GetMapping("/posts/{post_id}")
    @Operation(summary = "특정 게시글 조회", description = "해당 게시글의 정보를 불러온다")
    @ApiResponses(value = @ApiResponse(responseCode = "200", description = "성공"))
    public ResponseEntity<PostResponseDto> getPost(@PathVariable Long post_id) {
        PostResponseDto postResponseDto = postService.find(post_id);
        return ResponseEntity.ok(postResponseDto);
    }

    @PutMapping("/posts/{post_id}")
    @Operation(summary = "게시글 수정", description = "해당 게시글을 수정한다")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "403", description = "본인의 게시글만 수정 및 삭제가 가능합니다.")
    })
    public ResponseEntity<String> updatePost(@PathVariable Long post_id, @Valid @RequestBody PostUpdateRequestDto requestDto) {
        String result = postService.updatePost(post_id, requestDto);
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/posts/{post_id}")
    @Operation(summary = "게시글 삭제", description = "해당 게시글을 삭제한다")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "403", description = "본인의 게시글만 수정 및 삭제가 가능합니다.")
    })
    public ResponseEntity<String> deletePost(@PathVariable Long post_id) {
        String result = postService.deletePost(post_id);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/posts/search")
    @Operation(summary = "게시글 검색", description = "키워드로 게시글을 검색한다")
    @ApiResponses(value = @ApiResponse(responseCode = "200", description = "성공"))
    public ResponseEntity<List<PostResponseDto>> searchPostsByKeyword(@RequestParam String keyword) {
        List<PostResponseDto> postResponseDtoList = postService.searchByKeyword(keyword);
        return ResponseEntity.ok(postResponseDtoList);
    }
}
