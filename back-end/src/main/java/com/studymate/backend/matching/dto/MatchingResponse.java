package com.studymate.backend.matching.dto;

import com.studymate.backend.member.dto.MemberResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class MatchingResponse {
    private List<MemberResponse> memberResponses;  // 멘토들 리스트로 뿌려주기 위한 데이터
}
