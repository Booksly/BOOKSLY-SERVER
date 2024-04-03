package kyonggi.bookslyserver.domain.user.controller;

import jakarta.validation.Valid;
import kyonggi.bookslyserver.domain.user.dto.request.SendSMSRequestDto;
import kyonggi.bookslyserver.domain.user.dto.response.SendSMSResponseDto;
import kyonggi.bookslyserver.domain.user.service.UserAuthService;
import kyonggi.bookslyserver.global.auth.UserId;
import kyonggi.bookslyserver.global.common.SuccessResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/auth")
@RestController
@Validated
@Slf4j
public class UserAuthController {

    private final UserAuthService userAuthService;

    @PostMapping("/owner")
    public ResponseEntity<SuccessResponse<?>> sendSMSToOwner(@RequestBody @Valid SendSMSRequestDto sendSMSRequestDto) {
        SendSMSResponseDto sendSMSResponseDto = userAuthService.sendMessage(sendSMSRequestDto);

        return SuccessResponse.ok(sendSMSResponseDto);
    }

    @PostMapping("/user")
    public ResponseEntity<SuccessResponse<?>> sendSMSToUser(@UserId Long userId, @RequestBody @Valid SendSMSRequestDto sendSMSRequestDto) {
        SendSMSResponseDto sendSMSResponseDto = userAuthService.sendMessageToUser(userId, sendSMSRequestDto);

        return SuccessResponse.ok(sendSMSResponseDto);
    }
}
