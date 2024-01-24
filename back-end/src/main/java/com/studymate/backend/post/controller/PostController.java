package com.studymate.backend.post.controller;

import com.studymate.backend.member.domain.Member;
import com.studymate.backend.member.service.MemberService;
import com.studymate.backend.post.domain.Post;
import com.studymate.backend.post.dto.PostRequestDto;
import com.studymate.backend.post.dto.PostResponseDto;
import com.studymate.backend.post.service.PostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api")
@Tag(name = "Post", description = "Post API")
public class PostController {

    private final PostService postService;
    private final MemberService memberService;

    public PostController(PostService postService, MemberService memberService) {
        this.postService = postService;
        this.memberService = memberService;
    }

    @PostMapping("/posts")
    @Operation(summary = "post create", description = "게시글 생성")
    @ApiResponses(value = @ApiResponse(responseCode = "201", description = "성공"))
    public String create(@RequestBody PostRequestDto requestDto) {

        Member member = memberService.getMyMemberEntity();

        requestDto.setMember(member);
        Post post = postService.save(requestDto);

        return "success";
    }

    @GetMapping("/posts")
    @Operation(summary = "all posts read", description = "전체 게시글 읽기")
    @ApiResponses(value = @ApiResponse(responseCode = "200", description = "성공"))
    public List<PostResponseDto> list(@RequestParam(value = "nickname", required = false) String nickname){
        Member member = memberService.getMyMemberEntity();
        return postService.list(member, nickname);
    }

    @GetMapping("/posts/{id}")
    @Operation(summary = "post read", description = "해당 게시글 읽기")
    @ApiResponses(value = @ApiResponse(responseCode = "200", description = "성공"))
    public PostResponseDto get(@PathVariable Long id) {
        Member member = memberService.getMyMemberEntity();
        return postService.find(member, id);
    }

    @PutMapping("/posts/{id}")
    @Operation(summary = "post update", description = "게시글 수정")
    @ApiResponses(value = @ApiResponse(responseCode = "200", description = "성공"))
    public String update(@PathVariable Long id, @RequestBody PostRequestDto requestDto) {

        Member member = memberService.getMyMemberEntity();
        requestDto.setMember(member);
        return postService.update(id, requestDto);
    }

    @DeleteMapping("/posts/{id}")
    @Operation(summary = "post delete", description = "게시글 삭제")
    @ApiResponses(value = @ApiResponse(responseCode = "204", description = "성공"))
    public String delete(@PathVariable Long id) {
        Member currentMember = memberService.getMyMemberEntity();
        return postService.delete(id, currentMember);
    }
}
