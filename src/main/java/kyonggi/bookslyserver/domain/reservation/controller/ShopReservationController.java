package kyonggi.bookslyserver.domain.reservation.controller;

import kyonggi.bookslyserver.domain.reservation.dto.ReserveRequestDTO;
import kyonggi.bookslyserver.domain.reservation.service.ReserveOwnerCommandService;
import kyonggi.bookslyserver.global.common.SuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/shops/{shopId}")
public class ShopReservationController {
    private final ReserveOwnerCommandService reserveOwnerCommandService;
    @PostMapping("/reservation-settings")
    public ResponseEntity<SuccessResponse<?>> setReservationSetting
            (@PathVariable("shopId")Long shopId, @ModelAttribute ReserveRequestDTO.reservationSettingRequestDTO request){
        return SuccessResponse.created(reserveOwnerCommandService.setReservationSetting(request,shopId));
    }

    /**
     * 예약 요청 조회, 확정, 거절
     */
    @GetMapping("/reservation-requests/latest")
    public ResponseEntity<SuccessResponse<?>> getReservationRequest(@PathVariable("shopId")Long shopId){
        return SuccessResponse.ok(reserveOwnerCommandService.getReservationRequest(shopId));
    }
    @GetMapping("/reservation-requests/imminent")
    public ResponseEntity<SuccessResponse<?>> getImminentReservationRequest(@PathVariable("shopId")Long shopId){
        return SuccessResponse.ok(reserveOwnerCommandService.getImminentReservationRequest(shopId));
    }
    @GetMapping("/reservation-requests/details/latest")
    public ResponseEntity<SuccessResponse<?>> getReservationRequestDetails(@PathVariable("shopId")Long shopId){
        return SuccessResponse.ok(reserveOwnerCommandService.getReservationRequestDetails(shopId));
    }
    @GetMapping("/reservation-requests/details/imminent")
    public ResponseEntity<SuccessResponse<?>> getImminentReservationRequestDetails(@PathVariable("shopId")Long shopId){
        return SuccessResponse.ok(reserveOwnerCommandService.getImminentReservationRequestDetails(shopId));
    }
    @GetMapping("/reservation-requests/dates/{year}/{month}") // monthlyReq
    public ResponseEntity<SuccessResponse<?>> getDatesWithReservationRequest(@PathVariable("shopId")Long shopId,@PathVariable("year")int year,@PathVariable("month")int month){
        return SuccessResponse.ok(reserveOwnerCommandService.getDatesWithResRequest(year, month, shopId));
    }

    /**
     * 전체 직원용 예약 및 예약 일정 조회
     */
    @GetMapping("/reservations/all-employees/{date}")
    public ResponseEntity<SuccessResponse<?>> getTodayReservationSchedulesAllEmps(@PathVariable("date") LocalDate today, @PathVariable("shopId")Long shopId){
        return SuccessResponse.ok(reserveOwnerCommandService.getTodayReservationSchedulesAllEmps(today, shopId));
    }
    @GetMapping("reservations/of-date/all-employees/{date}")
    public ResponseEntity<SuccessResponse<?>> getReservationsOnlyAllEmpsOfDate(@PathVariable("date")LocalDate date, @PathVariable("shopId")Long shopId){
        return SuccessResponse.ok(reserveOwnerCommandService.getOnlyReservationsOfDateAllEmps(date, shopId));
    }


}