package kyonggi.bookslyserver.domain.user.controller;

import kyonggi.bookslyserver.domain.user.dto.response.GetUserNicknameResponseDto;
import kyonggi.bookslyserver.domain.user.service.UserService;
import kyonggi.bookslyserver.global.auth.principal.user.UserId;
import kyonggi.bookslyserver.global.common.SuccessResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/api/user")
@RestController
@Slf4j
public class UserController {

    private final UserService userService;


    @GetMapping("/test")
    public ResponseEntity<SuccessResponse<?>> test() {
        return SuccessResponse.ok("임시 리다이렉트 페이지");
    }

    @GetMapping("/details/nickname")
    public ResponseEntity<SuccessResponse<?>> getNickname(@UserId Long userId) {
        GetUserNicknameResponseDto getUserNicknameResponseDto = userService.getNickname(userId);
        return SuccessResponse.ok(getUserNicknameResponseDto);
    }
}
