package kyonggi.bookslyserver.domain.reservation.controller;

import kyonggi.bookslyserver.domain.reservation.dto.ReserveRequestDTO;
import kyonggi.bookslyserver.domain.reservation.service.ReserveCommandService;
import kyonggi.bookslyserver.global.common.SuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/reserve")
public class ShopOwnerReserveController {
    private final ReserveCommandService reserveCommandService;
    @PostMapping("/setting")
    public ResponseEntity<SuccessResponse<?>> setReservationSetting
            (@RequestParam("shopId")Long shopId, @ModelAttribute ReserveRequestDTO.reservationSettingRequestDTO request){
        return SuccessResponse.created(reserveCommandService.setReservationSetting(request,shopId));
    }
    @GetMapping("/closeTime")
    public ResponseEntity<SuccessResponse<?>> closeReservationSchedule(@RequestParam("scheduleId") Long reservationScheduleId){
        return SuccessResponse.ok(reserveCommandService.closeOrOpenReservationSchedule(reservationScheduleId));
    }
    /*
    * 임시 uri api 테스트 시에만 사용 바람
    */
    @GetMapping("/createResSch")
    public ResponseEntity<SuccessResponse<?>> createReservationSchedule(@RequestParam("employeeId")Long employeeId){
        return SuccessResponse.created(reserveCommandService.createEmployeeReservationSchedule(employeeId));
    }
}