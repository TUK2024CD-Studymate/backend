package com.studymate.backend.member.dto;

import com.studymate.backend.member.domain.Interests;
import com.studymate.backend.member.domain.Part;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;


@AllArgsConstructor
@Builder
@Getter
public class MemberResponse {
    private Long id;
    private String name;
    private String nickname;
    private Part part;
    private String email;
    private Interests interests;
//    private String imageUrl;
    private String blogUrl;
    private String publicRelations;
    private String job;
    private int heart;
    private double starAverage;
    private int solved;
    private int matchingCount;
}
