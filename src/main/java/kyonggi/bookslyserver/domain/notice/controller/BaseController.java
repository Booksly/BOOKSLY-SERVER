package kyonggi.bookslyserver.domain.notice.controller;

import kyonggi.bookslyserver.domain.notice.service.AuthService;
import kyonggi.bookslyserver.domain.notice.service.CustomMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


/**
 * 최초 카카오 로그인시 카카오 API에서 http://localhost:8080으로
 * reutrn해주는 code값을 받는 컨트롤러
 */
@RequestMapping("base")
@RestController
public class BaseController {

    @Autowired
    AuthService authService;

    @Autowired
    CustomMessageService customMessageService;

    /**
     * 나에게 메세지 보내기
     */
/*
    @GetMapping("")
    public String serviceStart(@RequestParam("code") String code) {
        if(authService.getKakaoAuthToken(code)) {
            customMessageService.sendMyMessage();
            return "메시지 전송 성공";
        }else {
            return "토큰 발급 실패";
        }
    }
 */

    /**
     * 친구 목록 가져오기
     */
/*
    @GetMapping("")
    public ResponseEntity<?> serviceStart(@RequestParam("code") String code) {
        if(authService.getKakaoAuthToken(code)) {
            return ResponseEntity.ok(customMessageService.getFriendsList());
        }else {
            return ResponseEntity.ok("토큰 발급 실패");
        }
    }
 */

    /**
     * 친구 목록 가져오기 + 가져온 친구 목록에 메세지 보내기
     */
    @GetMapping("")
    public ResponseEntity<?> serviceStart(@RequestParam("code") String code) {
        if(authService.getKakaoAuthToken(code)) {
            return customMessageService.sendMessageToFriend(customMessageService.getFriendsList());
        }else {
            return ResponseEntity.ok("토큰 발급 실패");
        }
    }
}
