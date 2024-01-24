package com.studymate.backend.post.service;

import com.studymate.backend.member.domain.Member;
import com.studymate.backend.member.service.MemberService;
import com.studymate.backend.post.domain.Post;
import com.studymate.backend.post.dto.PostRequestDto;
import com.studymate.backend.post.dto.PostResponseDto;
import com.studymate.backend.post.repository.PostRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@RequiredArgsConstructor
@Service
public class PostService {
    private final PostRepository postRepository;
    private final MemberService memberService;

    // 게시글 생성
    @Transactional
    public Post save(PostRequestDto requestDto){
        return postRepository.save(requestDto.toEntity());
    }

    // 게시글 리스트
    public List<PostResponseDto> list(Member member, String nickname) {
        List<Post> posts = postRepository.findAll();
        List<PostResponseDto> postList = new ArrayList<>();

        for (Post post : posts) {
            PostResponseDto postResponseDto = new PostResponseDto(post);
            postList.add(postResponseDto);
        }
        return postList;
    }

    // 게시글 조회
    public PostResponseDto find(Member member, Long id) {
        Post post = postRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다."));
        PostResponseDto postResponseDto = new PostResponseDto(post);
        return new PostResponseDto(post);
    }

    // 게시글 수정
    @Transactional
    public String update(Long id, PostRequestDto requestDto) {
        String message = "fail";
        Post post = postRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다."));
        Member currentMember = memberService.getMyMemberEntity();

        if(post.getMember().getId().equals(currentMember.getId())){
            post.update(requestDto.getContent());
            message = "success";
        }
        return message;

    }


    @Transactional
    public String delete(Long id, Member member){
        String message = "fail";
        Post post = postRepository.findById(id).orElseThrow(()-> new IllegalArgumentException("해당 게시글이 없습니다."));
        if (post.getMember().getId().equals(member.getId())){ // 작성자만 삭제 가능
            postRepository.delete(post);
            message = "success";
        }
        return message;
    }

}
