package com.studymate.backend.comment.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.studymate.backend.comment.domain.Comment;
import com.studymate.backend.file.ProfileImgRepository;
import com.studymate.backend.file.domain.ProfileImg;
import com.studymate.backend.member.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Optional;

@AllArgsConstructor
@Builder
@Getter
public class CommentResponse {
    private Long comment_id;

    private String content;

    private String profileUrl;

    private String nickname;

    private Long post_id;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm", timezone = "Asia/Seoul")
    private LocalDateTime createdAt;

    public static CommentResponse toResponse(Comment comment, ProfileImgRepository profileImgRepository) {
        String profileUrl = "https://studymate154.s3.ap-northeast-2.amazonaws.com/%ED%94%84%EB%A1%9C%ED%95%84+%EA%B8%B0%EB%B3%B8%EC%9D%B4%EB%AF%B8%EC%A7%80.png";
        if (comment == null) return null;

        Member member = comment.getMember();
        String nickname = (member != null) ? member.getNickname() : null;

        if (profileImgRepository.findByMember(member).isPresent()) {
            Optional<ProfileImg> profileImg = profileImgRepository.findByMember(member);
            profileUrl = profileImg.get().getUrl();
        }

        return CommentResponse.builder()
                .comment_id(comment.getId())
                .content(comment.getContent())
                .profileUrl(profileUrl)
                .nickname(nickname)
                .post_id(comment.getPost().getId())
                .createdAt(comment.getCreatedAt())
                .build();
    }
}
