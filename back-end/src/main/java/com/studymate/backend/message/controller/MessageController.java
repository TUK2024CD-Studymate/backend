package com.studymate.backend.message.controller;

import com.studymate.backend.message.dto.MessageRequest;
import com.studymate.backend.message.dto.VerifyRequest;
import com.studymate.backend.message.service.MessageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Tag(name = "회원", description = "회원 API")
public class MessageController {

    private final MessageService messageService;

    @PostMapping("/signIn/message")
    @Operation(summary = "인증번호 발송", description = "인증번호를 발송한다")
    @ApiResponses(value = @ApiResponse(responseCode = "200", description = "성공"))
    public ResponseEntity<?> sendSms(@Valid @RequestBody VerifyRequest request) {
        return ResponseEntity.ok(messageService.sendSMS(request));
    }

    @PostMapping("/signIn/message/verify")
    @Operation(summary = "인증번호 검증", description = "사용자가 입력한 인증번호를 검증한다")
    @ApiResponses(value = @ApiResponse(responseCode = "200", description = "성공"))
    public ResponseEntity<?> verifySms(@Valid @RequestBody MessageRequest request) {
        return ResponseEntity.ok(messageService.verifySms(request));
    }
}
