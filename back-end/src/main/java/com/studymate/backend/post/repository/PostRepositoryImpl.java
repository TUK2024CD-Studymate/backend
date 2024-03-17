package com.studymate.backend.post.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.studymate.backend.post.domain.Post;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import static com.studymate.backend.post.domain.QPost.*;

@Repository
@RequiredArgsConstructor
public class PostRepositoryImpl implements PostCustomRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public void addLikeCount(Post selectedPost) {
        queryFactory.update(post)
                .set(post.likeCount, post.likeCount.add(1))
                .where(post.eq(selectedPost))
                .execute();
    }

    @Override
    public void subLikeCount(Post selectedPost) {
        queryFactory.update(post)
                .set(post.likeCount, post.likeCount.subtract(1))
                .where(post.eq(selectedPost))
                .execute();
    }
}
