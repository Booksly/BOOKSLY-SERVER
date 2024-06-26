package kyonggi.bookslyserver.domain.notice.controller;

import kyonggi.bookslyserver.domain.notice.service.NoticeCommandService;
import kyonggi.bookslyserver.global.common.SuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/notices/shops/{shopId}")
public class ShopOwnerNoticeController {
    private final NoticeCommandService noticeCommandService;
    @GetMapping("/cancel")
    public ResponseEntity<SuccessResponse<?>> getCanceledReservationsNotices(@PathVariable("shopId")Long shopId){
        return SuccessResponse.ok(noticeCommandService.getCanceledReservationsNotices(shopId));
    }
    @GetMapping("/requests")
    public ResponseEntity<SuccessResponse<?>> getReservationRequestsNotices(@PathVariable("shopId")Long shopId){
        return SuccessResponse.ok(noticeCommandService.getReservationRequestsNotices(shopId));
    }
    @GetMapping("/delete/{noticeId}")
    public ResponseEntity<SuccessResponse<?>> deleteNotices(@PathVariable("shopId")Long shopId,@PathVariable("noticeId")Long noticeId){
        return SuccessResponse.ok(noticeCommandService.deleteNotices(noticeId,false));
    }

}
