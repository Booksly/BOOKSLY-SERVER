package kyonggi.bookslyserver.domain.user.controller;

import kyonggi.bookslyserver.domain.user.dto.response.GetUserDetailInfoResponseDto;
import kyonggi.bookslyserver.domain.user.dto.response.GetUserNicknameResponseDto;
import kyonggi.bookslyserver.domain.user.dto.response.GetUserProfileResponseDto;
import kyonggi.bookslyserver.domain.user.service.UserService;
import kyonggi.bookslyserver.global.auth.principal.user.UserId;
import kyonggi.bookslyserver.global.common.SuccessResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/api/users")
@RestController
@Slf4j
public class UserController {

    private final UserService userService;


    @GetMapping("/test")
    public ResponseEntity<SuccessResponse<?>> test() {
        return SuccessResponse.ok("임시 리다이렉트 페이지");
    }

    @GetMapping("/details")
    public ResponseEntity<SuccessResponse<?>> getUserDetailInfo(@UserId Long userId) {
        GetUserDetailInfoResponseDto getUserDetailInfoResponseDto = userService.getDetailInfo(userId);
        return SuccessResponse.ok(getUserDetailInfoResponseDto);
    }

    @GetMapping("/details/nickname")
    public ResponseEntity<SuccessResponse<?>> getNickname(@UserId Long userId) {
        GetUserNicknameResponseDto getUserNicknameResponseDto = userService.getNickname(userId);
        return SuccessResponse.ok(getUserNicknameResponseDto);
    }

    @GetMapping("/profile")
    public ResponseEntity<SuccessResponse<?>> getProfile(@UserId Long userId) {
        GetUserProfileResponseDto getProfileResponseDto = userService.getProfile(userId);
        return SuccessResponse.ok(getProfileResponseDto);
    }
}
