package kyonggi.bookslyserver.domain.user.controller;

import jakarta.validation.Valid;
import kyonggi.bookslyserver.domain.user.dto.request.VerifyCodeRequestDto;
import kyonggi.bookslyserver.domain.user.dto.request.SendSMSRequestDto;
import kyonggi.bookslyserver.domain.user.dto.response.CheckVerificationStatusResponseDto;
import kyonggi.bookslyserver.domain.user.dto.response.VerifyCodeResponseDto;
import kyonggi.bookslyserver.domain.user.dto.response.SendSMSResponseDto;
import kyonggi.bookslyserver.domain.user.service.UserAuthService;
import kyonggi.bookslyserver.global.auth.principal.user.UserId;
import kyonggi.bookslyserver.global.common.SuccessResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/api/auth/verify")
@RestController
@Validated
@Slf4j
public class UserVerifyController {

    private final UserAuthService userAuthService;

    @PostMapping("/owner/send-sms")
    public ResponseEntity<SuccessResponse<?>> sendSMSToOwner(@RequestBody @Valid SendSMSRequestDto sendSMSRequestDto) {
        SendSMSResponseDto sendSMSResponseDto = userAuthService.sendMessage(sendSMSRequestDto);

        return SuccessResponse.ok(sendSMSResponseDto);
    }

    @PostMapping("/user/send-sms")
    public ResponseEntity<SuccessResponse<?>> sendSMSToUser(@UserId Long userId, @RequestBody @Valid SendSMSRequestDto sendSMSRequestDto) {
        SendSMSResponseDto sendSMSResponseDto = userAuthService.sendMessageToUser(userId, sendSMSRequestDto);

        return SuccessResponse.ok(sendSMSResponseDto);
    }

    @PostMapping("/owner")
    public ResponseEntity<SuccessResponse<?>> ownerVerifyByCode(@RequestBody @Valid VerifyCodeRequestDto verifyCodeRequestDto) {
        VerifyCodeResponseDto verifyCodeResponseDto = userAuthService.ownerVerifyByCode(verifyCodeRequestDto);

        return SuccessResponse.ok(verifyCodeResponseDto);
    }

    @PostMapping("/user")
    public ResponseEntity<SuccessResponse<?>> userVerifyByCode(@UserId Long userId, @RequestBody @Valid VerifyCodeRequestDto verifyCodeRequestDto) {
        VerifyCodeResponseDto userVerifyResponseDto = userAuthService.userVerifyByCode(userId, verifyCodeRequestDto);

        return SuccessResponse.ok(userVerifyResponseDto);
    }

    @GetMapping("/user/status")
    public ResponseEntity<SuccessResponse<?>> checkVerificationStatus(@UserId Long userId) {
        CheckVerificationStatusResponseDto verificationStatusResponseDto = userAuthService.checkVerificationStatus(userId);

        return SuccessResponse.ok(verificationStatusResponseDto);
    }
}
