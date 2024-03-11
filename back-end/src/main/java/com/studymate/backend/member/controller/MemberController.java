package com.studymate.backend.member.controller;

import com.studymate.backend.commons.firebase.FCMTokenManager;
import com.studymate.backend.config.security.jwt.JwtFilter;
import com.studymate.backend.config.security.jwt.TokenProvider;
import com.studymate.backend.member.dto.*;
import com.studymate.backend.member.service.MemberService;
import com.studymate.backend.post.dto.PostResponseDto;
import com.studymate.backend.post.service.PostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Tag(name = "회원", description = "회원 API")
public class MemberController {

    private final TokenProvider tokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final MemberService memberService;
    private final PostService postService;
    private final FCMTokenManager fcmTokenManager;

    @PostMapping("/signIn")
    @Operation(summary = "회원가입", description = "회원이 회원가입을 한다.")
    @ApiResponses(value = @ApiResponse(responseCode = "200", description = "성공"))
    public ResponseEntity<?> signIn(@Valid @RequestBody MemberRequest request) {
        return ResponseEntity.ok(memberService.signup(request));
    }

    @PostMapping("/login")
    @Operation(summary = "로그인", description = "회원이 로그인을 한다.")
    @ApiResponses(value = @ApiResponse(responseCode = "200", description = "성공"))
    public ResponseEntity<TokenDto> authorizeWhitFCM(@Valid @RequestBody MemberLoginRequest request) {

        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword());

        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = tokenProvider.createToken(authentication);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(JwtFilter.AUTHORIZATION_HEADER, "Bearer " + jwt);

        memberService.fcmLogin(request);

        return new ResponseEntity<>(new TokenDto(jwt), httpHeaders, HttpStatus.OK);
    }

    @GetMapping("/user")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @Operation(summary = "회원정보 조회", description = "자신의 회원정보를 단건으로 조회한다")
    @ApiResponses(value = @ApiResponse(responseCode = "200", description = "성공"))
    public ResponseEntity<MemberResponse> getMyUserInfo() {
        return ResponseEntity.ok(memberService.getMyMemberWithAuthorities());
    }

    @PutMapping("/user")
    @PreAuthorize("hasAnyRole('USER')")
    @Operation(summary = "회원 정보 수정", description = "프로필 이미지를 제외한 회원정보를 수정한다")
    @ApiResponses(value = @ApiResponse(responseCode = "200", description = "성공"))
    public ResponseEntity<MemberResponse> update(@Valid @RequestBody MemberUpdateRequest request) {
        return ResponseEntity.ok(memberService.update(request));
    }

    @GetMapping("/user/post")
    @PreAuthorize("hasAnyRole('USER')")
    @Operation(summary = "작성한 게시물 조회", description = "자신이 작성한 게시물을 조회한다.")
    @ApiResponses(value = @ApiResponse(responseCode = "200", description = "성공"))
    public ResponseEntity<List<PostResponseDto>> getMyPost() {
        return ResponseEntity.ok(postService.findMemberPost());
    }

    @DeleteMapping("/user")
    @PreAuthorize("hasAnyRole('USER')")
    @Operation(summary = "회원탈퇴", description = "회원탈퇴(논리삭제)를 한다.")
    @ApiResponses(value = @ApiResponse(responseCode = "200", description = "성공"))
    public ResponseEntity<?> delete() {
        return ResponseEntity.ok(memberService.delete());
    }
}
