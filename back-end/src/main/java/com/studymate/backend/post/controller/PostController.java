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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@Tag(name = "Post", description = "Post API")
public class PostController {
    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @PostMapping("/posts")
    @Operation(summary = "post create", description = "게시글 생성")
    @ApiResponses(value = @ApiResponse(responseCode = "201", description = "성공"))
    public ResponseEntity<PostResponseDto> createPost(@Valid @RequestBody PostRequestDto requestDto) {
        PostResponseDto postResponseDto = postService.save(requestDto);
        return ResponseEntity.status(201).body(postResponseDto);
    }

    @GetMapping("/posts")
    @Operation(summary = "all posts read", description = "전체 게시글 읽기")
    @ApiResponses(value = @ApiResponse(responseCode = "200", description = "성공"))
    public ResponseEntity<List<PostResponseDto>> postList(){
        List<PostResponseDto> postResponseDtoList = postService.list();
        return ResponseEntity.ok(postResponseDtoList);
    }

    @GetMapping("/posts/{id}")
    @Operation(summary = "post read", description = "해당 게시글 읽기")
    @ApiResponses(value = @ApiResponse(responseCode = "200", description = "성공"))
    public ResponseEntity<PostResponseDto> getPost(@PathVariable Long id) {
        PostResponseDto postResponseDto = postService.find(id);
        return ResponseEntity.ok(postResponseDto);
    }

    @PutMapping("/posts/{id}")
    @Operation(summary = "post update", description = "게시글 수정")
    @ApiResponses(value = @ApiResponse(responseCode = "200", description = "성공"))
    public ResponseEntity<String> updatePost(@PathVariable Long id, @Valid @RequestBody PostUpdateRequestDto requestDto) {
        postService.updatePost(id, requestDto);
        return ResponseEntity.ok("업데이트 되었습니다.");
    }

    @DeleteMapping("/posts/{id}")
    @Operation(summary = "post delete", description = "게시글 삭제")
    @ApiResponses(value = @ApiResponse(responseCode = "200", description = "성공"))
    public ResponseEntity<String> deletePost(@PathVariable Long id) {
        postService.deletePost(id);
        return ResponseEntity.ok("삭제되었습니다.");
    }
}
