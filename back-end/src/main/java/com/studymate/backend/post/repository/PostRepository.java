package com.studymate.backend.post.repository;

import com.studymate.backend.heart.domain.Heart;
import com.studymate.backend.member.domain.Member;
import com.studymate.backend.post.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long>, PostCustomRepository {
    @Query("SELECT p FROM Post p WHERE p.content LIKE %:keyword% OR p.title LIKE %:keyword% OR p.member.nickname LIKE %:keyword%")
    List<Post> findByKeyword(@Param("keyword") String keyword);

    List<Post> findAllByMember(Member member);

    Post findByHeart(Heart heart);
}
