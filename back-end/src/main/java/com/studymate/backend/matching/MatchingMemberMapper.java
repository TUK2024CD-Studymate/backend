package com.studymate.backend.matching;

import com.studymate.backend.file.ProfileImgRepository;
import com.studymate.backend.file.domain.ProfileImg;
import com.studymate.backend.matching.dto.AiMatchingMemberResponse;
import com.studymate.backend.matching.dto.KmpMatchingMemberResponse;
import com.studymate.backend.member.domain.Member;
import com.studymate.backend.review.domain.Review;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class MatchingMemberMapper {

    private final ProfileImgRepository profileImgRepository;
    public AiMatchingMemberResponse toResponseForAi(Member member, Double percent, List<Review> reviewList) {
        String imageName = "https://studymate-bucket.s3.ap-northeast-2.amazonaws.com/profileImg.png";

        if (member == null) return null;

        if (profileImgRepository.findByMember(member).isPresent()) {
            Optional<ProfileImg> profileImg = profileImgRepository.findByMember(member);
            imageName = profileImg.get().getUrl();
        }

        return AiMatchingMemberResponse.builder()
                .id(member.getId())
                .email(member.getEmail())
                .part(member.getPart())
                .nickname(member.getNickname())
                .reviewCount(reviewList.size())
                .interests(member.getInterests())
                .imageUrl(imageName)
                .name(member.getName())
                .blogUrl(member.getBlogUrl())
                .expertiseField(member.getExpertiseField())
                .tel(member.getTel())
                .publicRelations(member.getPublicRelations())
                .job(member.getJob())
                .isLogin(member.isLogin())
                .heart(member.getHeart())
                .starAverage(member.getStarAverage())
                .solved(member.getSolved())
                .matchingCount(member.getMatchingCount())
                .matchingPercent(percent)
                .build();
    }

    public KmpMatchingMemberResponse toResponseForKmp(Member member, List<Review> reviewList) {
        String imageName = "https://studymate-bucket.s3.ap-northeast-2.amazonaws.com/profileImg.png";

        if (member == null) return null;

        if (profileImgRepository.findByMember(member).isPresent()) {
            Optional<ProfileImg> profileImg = profileImgRepository.findByMember(member);
            imageName = profileImg.get().getUrl();
        }

        return KmpMatchingMemberResponse.builder()
                .id(member.getId())
                .email(member.getEmail())
                .part(member.getPart())
                .nickname(member.getNickname())
                .reviewCount(reviewList.size())
                .interests(member.getInterests())
                .imageUrl(imageName)
                .name(member.getName())
                .blogUrl(member.getBlogUrl())
                .expertiseField(member.getExpertiseField())
                .tel(member.getTel())
                .publicRelations(member.getPublicRelations())
                .job(member.getJob())
                .isLogin(member.isLogin())
                .heart(member.getHeart())
                .starAverage(member.getStarAverage())
                .solved(member.getSolved())
                .matchingCount(member.getMatchingCount())
                .build();
    }
}
