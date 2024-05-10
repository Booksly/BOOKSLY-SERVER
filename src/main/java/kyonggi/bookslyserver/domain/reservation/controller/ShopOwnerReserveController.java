package kyonggi.bookslyserver.domain.reservation.controller;

import kyonggi.bookslyserver.domain.reservation.dto.ReserveRequestDTO;
import kyonggi.bookslyserver.domain.reservation.service.ReserveCommandService;
import kyonggi.bookslyserver.domain.reservation.service.ReserveOwnerCommandService;
import kyonggi.bookslyserver.global.common.SuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequiredArgsConstructor
@RequestMapping("/reserve-owner")
public class ShopOwnerReserveController {
    private final ReserveCommandService reserveCommandService;
    private final ReserveOwnerCommandService reserveOwnerCommandService;
    @PostMapping("/setting")
    public ResponseEntity<SuccessResponse<?>> setReservationSetting
            (@RequestParam("shopId")Long shopId, @ModelAttribute ReserveRequestDTO.reservationSettingRequestDTO request){
        return SuccessResponse.created(reserveCommandService.setReservationSetting(request,shopId));
    }

    @GetMapping("/closeTime")
    public ResponseEntity<SuccessResponse<?>> closeReservationSchedule(@RequestParam("scheduleId") Long reservationScheduleId){
        return SuccessResponse.ok(reserveCommandService.closeOrOpenReservationSchedule(reservationScheduleId));
    }

    @GetMapping("/latestRequest")
    public ResponseEntity<SuccessResponse<?>> getReservationRequest(@RequestParam("shopId")Long shopId){
        return SuccessResponse.ok(reserveOwnerCommandService.getReservationRequest(shopId));
    }
    @GetMapping("/imminentRequest")
    public ResponseEntity<SuccessResponse<?>> getImminentReservationRequest(@RequestParam("shopId")Long shopId){
        return SuccessResponse.ok(reserveOwnerCommandService.getImminentReservationRequest(shopId));
    }
    @GetMapping("/monthlyReq/{year}/{month}")
    public ResponseEntity<SuccessResponse<?>> getDatesWithReservationRequest(@RequestParam("shopId")Long shopId,@PathVariable("year")int year,@PathVariable("month")int month){
        return SuccessResponse.ok(reserveOwnerCommandService.getDatesWithResRequest(year, month, shopId));
    }
    @GetMapping("/confirmReq")
    public ResponseEntity<SuccessResponse<?>> confirmReservationRequest(@RequestParam("resId")Long reservationId){
        return SuccessResponse.ok(reserveOwnerCommandService.confirmReservationRequest(reservationId));
    }
    @PostMapping("/refuseReq")
    public ResponseEntity<SuccessResponse<?>> refuseReservationRequest(@RequestParam("resId")Long reservationId,@RequestBody ReserveRequestDTO.refuseReasonRequestDTO requestDTO){
        return SuccessResponse.ok(reserveOwnerCommandService.refuseReservationRequest(reservationId,requestDTO));
    }

    /**
     * 개별 직원용
     */
    @GetMapping("/todayReservations/{date}")
    public ResponseEntity<SuccessResponse<?>> getTodayReservationSchedules(@PathVariable("date") LocalDate today, @RequestParam("employeeId")Long employeeId){
        return SuccessResponse.ok(reserveOwnerCommandService.getTodayReservationSchedules(today, employeeId));
    }
    @GetMapping("/reservationsOfDate/{date}")
    public ResponseEntity<SuccessResponse<?>> getTodayReservationsOnly(@PathVariable("date") LocalDate date, @RequestParam("employeeId")Long employeeId){
        return SuccessResponse.ok(reserveOwnerCommandService.getTodayReservationsOnly(date, employeeId));
    }

    /**
     * 전체 직원용
     */
    @GetMapping("/todayReservationsAll/{date}")
    public ResponseEntity<SuccessResponse<?>> getTodayReservationSchedulesAllEmps(@PathVariable("date") LocalDate today, @RequestParam("shopId")Long shopId){
        return SuccessResponse.ok(reserveOwnerCommandService.getTodayReservationSchedulesAllEmps(today, shopId));
    }
    @GetMapping("reservationsOfDateAll/{date}")
    public ResponseEntity<SuccessResponse<?>> getTodayReservationsOnlyAllEmps(@PathVariable("date")LocalDate date, @RequestParam("shopId")Long shopId){
        return SuccessResponse.ok(reserveOwnerCommandService.getOnlyReservationsOfDateAllEmps(date, shopId));
    }

    @GetMapping("todayReservationDetails/{date}")
    public ResponseEntity<SuccessResponse<?>> getTodayReservationsDetails(@PathVariable("date")LocalDate today,@RequestParam("employeeId")Long employeeId){
        return SuccessResponse.ok(reserveOwnerCommandService.getTodayReservationsDetails(today, employeeId));
    }
    @GetMapping("reservationDetails/{date}")
    public ResponseEntity<SuccessResponse<?>> getReservationDetailsOfDate(@PathVariable("date")LocalDate date,@RequestParam("employeeId")Long employeeId){
        return SuccessResponse.ok(reserveOwnerCommandService.getReservationsOfDateDetails(date, employeeId));
    }
    /*
    * 임시 uri api 테스트 시에만 사용 바람
    */
    @GetMapping("/createResSch")
    public ResponseEntity<SuccessResponse<?>> createReservationSchedule(@RequestParam("employeeId")Long employeeId){
        return SuccessResponse.created(reserveCommandService.createEmployeeReservationSchedule(employeeId));
    }
}