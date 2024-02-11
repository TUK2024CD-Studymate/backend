package com.studymate.backend.comment.controller;

import com.studymate.backend.comment.dto.CommentRequestDto;
import com.studymate.backend.comment.dto.CommentResponseDto;
import com.studymate.backend.comment.service.CommentService;
import com.studymate.backend.comment.service.CommentServiceValidator;
import com.studymate.backend.post.domain.Post;
import com.studymate.backend.post.service.PostService;
import com.studymate.backend.post.service.ServiceValidator;
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
@Tag(name = "Comment", description = "Comment API")
public class CommentController {
    private final CommentService commentService;
    private final PostService postService;
    private final ServiceValidator serviceValidator;
    private final CommentServiceValidator commentServiceValidator;

    @PostMapping("/posts/{post_id}/comments")
    @Operation(summary = "comment create", description = "댓글 생성")
    @ApiResponses(value = @ApiResponse(responseCode = "201", description = "성공"))
    public ResponseEntity<CommentResponseDto> createComment(@PathVariable("post_id") Long postId,
                                                            @Valid @RequestBody CommentRequestDto requestDto){
        Post post = postService.getPost(postId);
        CommentResponseDto commentResponseDto = commentService.save(requestDto, post);
        return ResponseEntity.status(HttpStatus.CREATED).body(commentResponseDto);
    }

    @GetMapping("/posts/{post_id}/comments")
    @Operation(summary = "comments read", description = "해당 게시글의 댓글 읽기")
    @ApiResponses(value = @ApiResponse(responseCode = "200", description = "성공"))
    public ResponseEntity<List<CommentResponseDto>> getCommentsByPostId(@PathVariable("post_id") Long postId){
        List<CommentResponseDto> comments = commentService.list(postId);
        return ResponseEntity.ok(comments);
    }

    @PutMapping("/posts/{post_id}/comments/{comment_id}")
    @Operation(summary = "comment update", description = "댓글 수정")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "403", description = "본인의 댓글만 수정 및 삭제가 가능합니다.")
    })
    public ResponseEntity<String> updateComment(@PathVariable("post_id") Long postId,
                                                @PathVariable("comment_id") Long commentId,
                                                @Valid @RequestBody CommentRequestDto requestDto){
        commentServiceValidator.validateCommentBelongsToPost(commentId, postId);
        String result = commentService.updateComment(commentId, requestDto);
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/posts/{post_id}/comments/{comment_id}")
    @Operation(summary = "comment delete", description = "댓글 삭제")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "403", description = "본인의 댓글만 수정 및 삭제가 가능합니다.")
    })
    public ResponseEntity<String> deletePost(@PathVariable("post_id") Long postId,
                                             @PathVariable("comment_id") Long commentId){
        commentServiceValidator.validateCommentBelongsToPost(commentId, postId);
        String result = commentService.deleteComment(commentId);
        return ResponseEntity.ok(result);
    }
}
