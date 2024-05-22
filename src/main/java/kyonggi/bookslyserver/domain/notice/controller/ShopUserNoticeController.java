package kyonggi.bookslyserver.domain.notice.controller;

import kyonggi.bookslyserver.domain.notice.service.NoticeCommandService;
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
    private final NoticeCommandService noticeCommandService;
    @GetMapping("/refuse")
    public ResponseEntity<SuccessResponse<?>> getRefusedReservationsNotices(@UserId Long userId){
        return SuccessResponse.ok(noticeCommandService.getRefusedReservationsNotices(userId));
    }
    @GetMapping("/confirm")
    public ResponseEntity<SuccessResponse<?>> getConfirmedReservationsNotices(@UserId Long userId){
        return SuccessResponse.ok(noticeCommandService.getConfirmedReservationsNotices(userId));
    }
    @GetMapping("/todo")
    public ResponseEntity<SuccessResponse<?>> getTodoReservationsNotices(@UserId Long userId){
        return SuccessResponse.ok(noticeCommandService.getTodoReservationsNotices(userId));
    }
}
