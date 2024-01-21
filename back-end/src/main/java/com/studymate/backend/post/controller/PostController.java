package com.studymate.backend.post.controller;

import com.studymate.backend.post.domain.entity.Member;
import com.studymate.backend.post.dto.PostDto;
import com.studymate.backend.post.repository.MemberRepository;
import com.studymate.backend.post.service.PostService;
import lombok.RequiredArgsConstructor;
import com.studymate.backend.global.Response;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;
    private final MemberRepository memberRepository;

    // 전체 게시글 조회
    //@ApiOperation(value = "전체 게시글 보기", notes = "전체 게시글을 조회한다.")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/posts")
    public Response getPosts(){
        return new Response("성공", "전체 게시물 리턴", postService.getPosts());
    }

    // 개별 게시글 조회
    //@ApiOperation(value = "개별 게시글 보기", notes = "개별 게시글 조회한다.")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/posts/{id}")
    public Response getpost(@PathVariable("id") Integer id) {
        return new Response("성공", "개별 게시물 리턴", postService.getPost(id));
    }

    // 게시글 작성
    //@ApiOperation(value = "게시글 작성", notes = "게시글을 작성한다.")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/posts")
    public Response createPost(@RequestBody PostDto postDto){
        Member member = memberRepository.findById(1).get();
        return new Response("성공", "글 작성 성공", postService.createPost(postDto, member));
    }

    // 게시글 수정
    //@ApiOperation(value = "게시글 수정", notes = "게시글을 수정한다.")
    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/users/{user-id}/post/{post-id}")
    public Response edit(@RequestBody PostDto postDto, @PathVariable("id") Integer id) {

        // 임의로 findById 로 유저 정보를 넣어줬습니다.
        // 추후에 JWT 로그인을 배우고나서 적용해야할 것

        // 1. 현재 요청을 보낸 유저의 JWT 토큰 정보(프론트엔드가 헤더를 통해 보내줌)를 바탕으로
        // 현재 로그인한 유저의 정보가 PathVariable로 들어오는 PostID 의 작성자인 user정보와 일치하는지 확인하고
        // 맞으면 아래 로직 수행, 틀리면 다른 로직(ResponseFail 등 커스텀으로 만들어서) 수행
        // 이건 if문으로 처리할 수 있습니다. * 이 방법 말고 service 내부에서 확인해도 상관 없음

        Member user = memberRepository.findById(1).get();
        return new Response("성공", "글 수정 성공", postService.update(id, postDto));
    }

    // 게시글 삭제
    //@ApiOperation(value = "게시글 삭제", notes = "게시글을 삭제한다.")
    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("/users/{user-id}/post/{post-id}")
    public Response delete(@PathVariable("id") Integer id) {
        // 현재 이 요청을 보낸 로그인된 유저의 정보가
        // 게시글의 주인인지 확인하고, 맞으면 삭제 수행 후 리턴해주고, 틀리면 에러 리턴 (jwt 맞춰서 수정할게요 ㅠㅠ)

        postService.delete(id); // 이 메소드는 반환값이 없으므로 따로 삭제 수행해주고, 리턴에는 null을 넣어줌
        return new Response("성공", "글 삭제 성공", null);
    }


}
