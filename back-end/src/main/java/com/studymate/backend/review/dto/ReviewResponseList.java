package com.studymate.backend.review.dto;

import lombok.*;

import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ReviewResponseList {
    private List<ReviewResponse> reviewResponses;
    private int reviewCount;
}
