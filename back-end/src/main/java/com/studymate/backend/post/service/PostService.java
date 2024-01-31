package com.studymate.backend.post.service;

import com.studymate.backend.member.MemberRepository;
import com.studymate.backend.member.domain.Member;
import com.studymate.backend.post.PostMapper;
import com.studymate.backend.post.domain.Post;
import com.studymate.backend.post.dto.PostRequestDto;
import com.studymate.backend.post.dto.PostResponseDto;
import com.studymate.backend.post.dto.PostUpdateRequestDto;
import com.studymate.backend.post.repository.PostRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class PostService {
    private final PostRepository postRepository;
    private final PostMapper postMapper;
    private final MemberRepository memberRepository;

    // 게시글 생성
    @Transactional
    public PostResponseDto save(PostRequestDto requestDto){
        Member member = memberRepository.findById(requestDto.getUser_id())
                .orElseThrow(() -> new RuntimeException("Member not found"));
        Post post = postMapper.toEntity(requestDto, member);
        Post savedPost = postRepository.save(post);
        return postMapper.toResponse(savedPost);
    }

    // 게시글 리스트
    public List<PostResponseDto> list() {
        List<Post> posts = postRepository.findAll();
        return posts.stream()
                .map(postMapper::toResponse)
                .collect(Collectors.toList());
    }

    // 게시글 조회
    public PostResponseDto find(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다."));
        return postMapper.toResponse(post);
    }

    // 게시글 수정
    @Transactional
    public String updatePost(Long id, PostUpdateRequestDto requestDto, String userEmail) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다."));

//        System.out.println("User Email: " + userEmail);
//        System.out.println("Post Owner Email: " + post.getMember().getEmail());
//
//        // 게시글의 사용자 이메일과 현재 인증된 사용자의 이메일을 비교
//        if (!post.getMember().getEmail().equals(userEmail)) {
//            throw new IllegalArgumentException("본인의 게시글만 수정할 수 있습니다.");
//        }
        post.update(requestDto.getTitle(), requestDto.getContent(), requestDto.getCategory());
        return "success";
    }

    // 게시글 삭제
    @Transactional
    public String deletePost(Long id, String userEmail){
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다."));

//        // 게시글의 사용자 이메일과 현재 인증된 사용자의 이메일을 비교
//        if (!post.getMember().getEmail().equals(userEmail)) {
//            throw new IllegalArgumentException("본인의 게시글만 삭제할 수 있습니다.");
//        }

        postRepository.delete(post);
        return "success";
    }
}
