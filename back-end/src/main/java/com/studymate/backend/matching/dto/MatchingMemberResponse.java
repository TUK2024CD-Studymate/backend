package com.studymate.backend.matching.dto;

import com.studymate.backend.member.domain.Interests;
import com.studymate.backend.member.domain.Part;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MatchingMemberResponse {
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
    private Double matchingPercent;
}
