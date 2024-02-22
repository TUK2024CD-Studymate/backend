package com.studymate.backend.matching.controller;

import com.studymate.backend.matching.service.MatchingService;
import com.studymate.backend.member.dto.MemberListResponse;
import com.studymate.backend.member.dto.MemberResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class MatchingController {

    private final MatchingService matchingService;

    @GetMapping("/matching/{questionId}")
    public ResponseEntity<MemberListResponse> getMentorList(@PathVariable("questionId")Long questionId) {
        return ResponseEntity.ok().body(matchingService.getMentorList(questionId));
    }
}
