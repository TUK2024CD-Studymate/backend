package com.studymate.backend.matching.dto;

import com.studymate.backend.member.domain.Interests;
import com.studymate.backend.member.domain.Part;
import lombok.*;

import java.math.BigDecimal;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class KmpMatchingMemberResponse {
    private Long id;
    private String name;
    private String nickname;
    private Part part;
    private String email;
    private String tel;
    private String expertiseField;
    private Interests interests;
    private String imageUrl;
    private String blogUrl;
    private String publicRelations;
    private String job;
    private int heart;
    private BigDecimal starAverage;
    private int solved;
    private int matchingCount;
    private int reviewCount;
    private boolean isLogin;
}
