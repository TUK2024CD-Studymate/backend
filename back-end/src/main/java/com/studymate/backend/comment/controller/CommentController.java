package com.studymate.backend.comment.controller;

import com.studymate.backend.comment.dto.request.CommentRequest;
import com.studymate.backend.comment.dto.response.CommentResponse;
import com.studymate.backend.comment.service.CommentService;
import com.studymate.backend.post.domain.Post;
import com.studymate.backend.post.repository.PostRepository;
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
@RequiredArgsConstructor
@RequestMapping("/api")
@Tag(name = "댓글", description = "댓글 API")
public class CommentController {
    private final CommentService commentService;
    private final PostRepository postRepository;


    @PostMapping("/posts/{post_id}/comments")
    @Operation(summary = "댓글 생성", description = "회원이 댓글을 생성한다")
    @ApiResponses(value = @ApiResponse(responseCode = "201", description = "성공"))
    public ResponseEntity<CommentResponse> createComment(@PathVariable("post_id") Long id,
                                                         @Valid @RequestBody CommentRequest requestDto){
        Post post = postRepository.findById(id).orElseThrow(IllegalStateException::new);
        CommentResponse commentResponseDto = commentService.save(requestDto, post);
        return ResponseEntity.status(HttpStatus.CREATED).body(commentResponseDto);
    }

    @GetMapping("/posts/{post_id}/comments")
    @Operation(summary = "댓글 조회", description = "해당 게시글의 댓글을 불러온다")
    @ApiResponses(value = @ApiResponse(responseCode = "200", description = "성공"))
    public ResponseEntity<List<CommentResponse>> getCommentsByPostId(@PathVariable("post_id") Long postId){
        List<CommentResponse> comments = commentService.list(postId);
        return ResponseEntity.ok(comments);
    }

    @PutMapping("/posts/{post_id}/comments/{comment_id}")
    @Operation(summary = "댓글 수정", description = "회원이 작성한 댓글을 수정한다")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "403", description = "본인의 댓글만 수정 및 삭제가 가능합니다.")
    })
    public ResponseEntity<String> updateComment(@PathVariable("post_id") Long postId,
                                                @PathVariable("comment_id") Long commentId,
                                                @Valid @RequestBody CommentRequest requestDto){
        String result = commentService.updateComment(postId, commentId, requestDto);
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/posts/{post_id}/comments/{comment_id}")
    @Operation(summary = "댓글 삭제", description = "회원이 작성한 댓글을 삭제한다")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "403", description = "본인의 댓글만 수정 및 삭제가 가능합니다.")
    })
    public ResponseEntity<String> deletePost(@PathVariable("post_id") Long postId,
                                             @PathVariable("comment_id") Long commentId){
        String result = commentService.deleteComment(postId, commentId);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/posts/{post_id}/comments/count")
    @Operation(summary = "댓글 수 조회", description = "해당 게시글의 댓글 수를 반환한다")
    @ApiResponses(value = @ApiResponse(responseCode = "200", description = "성공"))
    public ResponseEntity<Long> countCommentsByPostId(@PathVariable("post_id") Long postId) {
        Long count = commentService.countCommentsByPostId(postId);
        return ResponseEntity.ok(count);
    }

}
