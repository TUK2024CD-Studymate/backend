package com.studymate.backend.post.service;

import com.studymate.backend.post.domain.entity.Member;
import com.studymate.backend.post.domain.entity.Post;
import com.studymate.backend.post.dto.PostDto;
import com.studymate.backend.post.repository.PostRepository;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class PostService {
    private final PostRepository postRepository;

    // 전체 게시물 조회
    @Transactional(readOnly = true)
    public List<PostDto> getPosts() {
        List<Post> posts = postRepository.findAll();
        List<PostDto> postDtos = new ArrayList<>();
        posts.forEach(s -> postDtos.add(PostDto.toDto(s)));
        return postDtos;
    }

    // 개별 게시물 조회
    @Transactional(readOnly = true)
    public PostDto getPost(int id) {
        Post post = postRepository.findById(id).orElseThrow(() -> {
            return new IllegalArgumentException("Post Id를 찾을 수 없습니다.");
        });
        PostDto postDto = PostDto.toDto(post);
        return postDto;
    }

    // 게시물 작성
    @Transactional
    public PostDto createPost(PostDto postDto, Member member) {
        Post post = new Post();
        post.setTitle(postDto.getTitle());
        post.setContent(postDto.getContent());
        post.setMember(member);
        postRepository.save(post);
        return PostDto.toDto(post);
    }

    // 게시물 수정
    @Transactional
    public PostDto update(int id, PostDto postDto) {
        Post post = postRepository.findById(id).orElseThrow(() -> {
            return new IllegalArgumentException("Post Id를 찾을 수 없습니다.");
        });

        post.setTitle(postDto.getTitle());
        post.setContent(postDto.getContent());

        return PostDto.toDto(post);
    }


    // 게시글 삭제
    @Transactional
    public void delete(int id) {
        // 매개변수 id를 기반으로, 게시글이 존재하는지 먼저 찾음
        // 게시글이 없으면 오류 처리
        Post post = postRepository.findById(id).orElseThrow(() -> {
            return new IllegalArgumentException("Post Id를 찾을 수 없습니다");
        });

        // 게시글이 있는 경우 삭제처리
        postRepository.deleteById(id);
    }
}