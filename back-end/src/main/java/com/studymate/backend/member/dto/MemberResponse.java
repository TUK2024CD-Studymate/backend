package com.studymate.backend.member.dto;

import com.studymate.backend.member.domain.Interests;
import com.studymate.backend.member.domain.Part;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;


@AllArgsConstructor
@Builder
@Getter
public class MemberResponse {
    private Long id;
    private String name;
    private String nickname;
    private Part part;
    private String email;
    private String tel;
    private Interests interests;
//    private String imageUrl;
    private String blogUrl;
    private String publicRelations;
    private String job;
    private int heart;
    private BigDecimal starAverage;
    private int solved;
    private int matchingCount;
}
