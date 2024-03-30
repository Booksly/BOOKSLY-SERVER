package kyonggi.bookslyserver.domain.user.controller;

import kyonggi.bookslyserver.global.auth.UserId;
import kyonggi.bookslyserver.global.common.SuccessResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/user")
@RestController
@Slf4j
public class UserController {


    @GetMapping("/test")
    public ResponseEntity<SuccessResponse<?>> test() {
        return SuccessResponse.ok("임시 리다이렉트 페이지");
    }

    @GetMapping("/need-login")
    public ResponseEntity<SuccessResponse<?>> needLogin(@UserId Long userId) {
        return SuccessResponse.ok("유저 아이디 = "+userId );
    }
}
