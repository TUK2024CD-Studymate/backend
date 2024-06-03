package com.studymate.backend.matching;

import com.studymate.backend.file.ProfileImgRepository;
import com.studymate.backend.file.domain.ProfileImg;
import com.studymate.backend.matching.dto.MatchingMemberResponse;
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
    public MatchingMemberResponse toResponse(Member member, Double percent, List<Review> reviewList) {
        String imageName = "프로필 사진이 없습니다";

        if (member == null) return null;

        if (profileImgRepository.findByMember(member).isPresent()) {
            Optional<ProfileImg> profileImg = profileImgRepository.findByMember(member);
            imageName = profileImg.get().getUrl();
        }

        return MatchingMemberResponse.builder()
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
}
