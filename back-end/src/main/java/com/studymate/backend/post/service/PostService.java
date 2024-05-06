package com.studymate.backend.post.service;

import com.studymate.backend.heart.HeartRepository;
import com.studymate.backend.heart.domain.Heart;
import com.studymate.backend.member.domain.Member;
import com.studymate.backend.member.service.MemberService;
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
    private final MemberService memberService;
    private final ServiceValidator serviceValidator;

    private final HeartRepository heartRepository;

    // 게시글 생성
    @Transactional
    public PostResponseDto save(PostRequestDto requestDto) {
        Member member = memberService.getMember(); // 현재 인증된 회원 확인

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
    public PostResponseDto find(Long post_id) {
        Post post = postRepository.findById(post_id)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다."));
        return postMapper.toResponse(post);
    }

    // 게시글 수정
    @Transactional
    public String updatePost(Long post_id, PostUpdateRequestDto requestDto) {
        Member member = memberService.getMember(); // 현재 인증된 회원 확인
        Post post = serviceValidator.validatePostOwnership(post_id, member); // 게시글 소유권 검증

        post.update(requestDto.getTitle(), requestDto.getContent(), requestDto.getCategory(), requestDto.getInterests(), requestDto.getRecruitmentStatus());
        return "success";
    }

    // 게시글 삭제
    @Transactional
    public String deletePost(Long post_id){
        Member member = memberService.getMember();
        Post post = serviceValidator.validatePostOwnership(post_id, member);
        postRepository.delete(post);
        return "success";
    }

    public List<PostResponseDto> searchByKeyword(String keyword) {
        List<Post> posts = postRepository.findByKeyword(keyword);
        return posts.stream()
                .map(postMapper::toResponse)
                .collect(Collectors.toList());
    }

    // 내가 쓴 게시물 조회
    public List<PostResponseDto> findMemberPost() {
        Member member = memberService.getMember();
        List<Post> posts = postRepository.findAllByMember(member);
        return posts.stream().map(postMapper::toResponse).collect(Collectors.toList());
    }

    public List<PostResponseDto> getHeartPost() {
        Member member = memberService.getMember();

        List<Heart> heart = heartRepository.findAllByMember(member);

        List<Post> posts = heart.stream().map(postRepository::findByHeart).toList();

        return posts.stream().map(postMapper::toResponse).collect(Collectors.toList());
    }
}
