package kyonggi.bookslyserver.domain.reservation.controller;

import kyonggi.bookslyserver.domain.reservation.dto.ReserveRequestDTO;
import kyonggi.bookslyserver.domain.reservation.service.ReserveCommandService;
import kyonggi.bookslyserver.domain.reservation.service.ReserveOwnerCommandService;
import kyonggi.bookslyserver.global.auth.principal.user.UserId;
import kyonggi.bookslyserver.global.common.SuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/reservations")
public class ReservationController {
    private final ReserveCommandService reserveCommandService;
    private final ReserveOwnerCommandService reserveOwnerCommandService;

    /**
     * 직원별, 날짜별 가능한 시간대 조회
     */
    @GetMapping("/time")
    public ResponseEntity<SuccessResponse<?>> getAvailableReservationTimes(@RequestParam("employeeId")Long employeeId, @RequestParam("date")LocalDate date){
        return SuccessResponse.ok(reserveCommandService.getAvailableReservationTimes(employeeId,date));
    }

    /**
     * 예약하기
     */
    @PostMapping("/")
    public ResponseEntity<SuccessResponse<?>> createReservation(@UserId Long userId, @RequestBody ReserveRequestDTO.reservationRequestDTO request){
        return SuccessResponse.created(reserveCommandService.createReservation(userId, request));
    }

    /**
     * 당일 예약 예약 조회
     */
    @GetMapping("/todayReservations/{today}")
    public ResponseEntity<SuccessResponse<?>> findTodayReservation
            (@PathVariable("today")LocalDate date, @RequestParam List<LocalTime> startTimes,@RequestParam List<LocalTime> endTimes, @RequestParam List<Long> categories){
        return SuccessResponse.ok(reserveCommandService.findTodayReservation(date,startTimes,endTimes,categories));
    }
    @GetMapping("/todayReservations/discount/{today}")
    public ResponseEntity<SuccessResponse<?>> findTodayReservationByDiscount
            (@PathVariable("today")LocalDate date, @RequestParam List<LocalTime> startTimes,@RequestParam List<LocalTime> endTimes, @RequestParam List<Long> categories){
        return SuccessResponse.ok(reserveCommandService.findTodayReservationByDiscount(date,startTimes,endTimes,categories));
    }

    /**
     * 예약 마감 
     */
    @GetMapping("/closeTime")
    public ResponseEntity<SuccessResponse<?>> closeReservationSchedule(@RequestParam("scheduleId") Long reservationScheduleId){
        return SuccessResponse.ok(reserveOwnerCommandService.closeOrOpenReservationSchedule(reservationScheduleId));
    }
    @GetMapping("/confirmReq")
    public ResponseEntity<SuccessResponse<?>> confirmReservationRequest(@RequestParam("resId")Long reservationId){
        return SuccessResponse.ok(reserveOwnerCommandService.confirmReservationRequest(reservationId));
    }
    @PostMapping("/refuseReq")
    public ResponseEntity<SuccessResponse<?>> refuseReservationRequest(@RequestParam("resId")Long reservationId,@RequestBody ReserveRequestDTO.refuseReasonRequestDTO requestDTO){
        return SuccessResponse.ok(reserveOwnerCommandService.refuseReservationRequest(reservationId,requestDTO));
    }

}
