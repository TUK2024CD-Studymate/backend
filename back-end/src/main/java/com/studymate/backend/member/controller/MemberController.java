package com.studymate.backend.member.controller;

import com.studymate.backend.config.security.jwt.JwtFilter;
import com.studymate.backend.config.security.jwt.TokenProvider;
import com.studymate.backend.member.dto.*;
import com.studymate.backend.member.service.MemberService;
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

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Tag(name = "Member", description = "Member API")
public class MemberController {

    private final TokenProvider tokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final MemberService memberService;

    @PostMapping("/signIn")
    @Operation(summary = "member sign in", description = "회원이 회원가입을 한다.")
    @ApiResponses(value = @ApiResponse(responseCode = "200", description = "성공"))
    public ResponseEntity<?> signIn(@Valid @RequestBody MemberRequest request) {
        return ResponseEntity.ok(memberService.signup(request));
    }

    @PostMapping("/login")
    @Operation(summary = "member login", description = "회원이 로그인을 한다.")
    @ApiResponses(value = @ApiResponse(responseCode = "200", description = "성공"))
    public ResponseEntity<TokenDto> authorize(@Valid @RequestBody MemberLoginRequest request) {

        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword());

        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = tokenProvider.createToken(authentication);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(JwtFilter.AUTHORIZATION_HEADER, "Bearer " + jwt);

        return new ResponseEntity<>(new TokenDto(jwt), httpHeaders, HttpStatus.OK);
    }

    @GetMapping("/user")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @Operation(summary = "get member info", description = "회원정보를 단건으로 조회한다")
    @ApiResponses(value = @ApiResponse(responseCode = "200", description = "성공"))
    public ResponseEntity<MemberResponse> getMyUserInfo() {
        return ResponseEntity.ok(memberService.getMyMemberWithAuthorities());
    }

    @PutMapping("/user")
    @PreAuthorize("hasAnyRole('USER')")
    @Operation(summary = "member update", description = "프로필 이미지를 제외한 회원정보를 수정한다")
    @ApiResponses(value = @ApiResponse(responseCode = "200", description = "성공"))
    public ResponseEntity<MemberResponse> update(@Valid @RequestBody MemberUpdateRequest request) {
        return ResponseEntity.ok(memberService.update(request));
    }

    @DeleteMapping("/user")
    @PreAuthorize("hasAnyRole('USER')")
    @Operation(summary = "member soft delete", description = "회원탈퇴(논리삭제)를 한다.")
    @ApiResponses(value = @ApiResponse(responseCode = "200", description = "성공"))
    public ResponseEntity<?> delete() {
        return ResponseEntity.ok(memberService.delete());
    }
}
