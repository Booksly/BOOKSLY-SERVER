package kyonggi.bookslyserver.domain.notice.controller;

import kyonggi.bookslyserver.global.auth.principal.user.UserId;
import kyonggi.bookslyserver.global.common.SuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/notices")
public class ShopUserNoticeController {
    @GetMapping("/refuse")
    public ResponseEntity<SuccessResponse<?>> getRefusedReservationsNotices(@UserId Long userId){
        return SuccessResponse.ok("");
    }
}
