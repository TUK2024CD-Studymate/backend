package com.studymate.backend.post.repository;

import com.studymate.backend.post.domain.Post;

public interface PostCustomRepository {

    void addLikeCount(Post post);

    void subLikeCount(Post post);
}
