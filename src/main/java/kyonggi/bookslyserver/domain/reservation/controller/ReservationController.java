package kyonggi.bookslyserver.domain.reservation.controller;

import kyonggi.bookslyserver.domain.reservation.dto.ReserveRequestDTO;
import kyonggi.bookslyserver.domain.reservation.service.ReserveCommandService;
import kyonggi.bookslyserver.domain.reservation.service.ReserveOwnerCommandService;
import kyonggi.bookslyserver.global.auth.principal.user.UserId;
import kyonggi.bookslyserver.global.common.SuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import retrofit2.http.Path;

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
    @GetMapping("/available-times")
    public ResponseEntity<SuccessResponse<?>> getAvailableReservationTimes(@RequestParam("employeeId")Long employeeId, @RequestParam("date")LocalDate date){
        return SuccessResponse.ok(reserveCommandService.getAvailableReservationTimes(employeeId,date));
    }

    /**
     * 예약하기
     */
    @PostMapping("")
    public ResponseEntity<SuccessResponse<?>> createReservation(@UserId Long userId, @RequestBody ReserveRequestDTO.reservationRequestDTO request){
        return SuccessResponse.created(reserveCommandService.createReservation(userId, request));
    }

    /**
     * 당일 예약 예약 조회
     */
    @GetMapping("/today/{today}")
    public ResponseEntity<SuccessResponse<?>> findTodayReservation
            (@PathVariable("today")LocalDate date,@RequestParam List<String> firstAddress,@RequestParam List<String> secondAddress,@RequestParam List<String> thirdAddress, @RequestParam List<LocalTime> startTimes,@RequestParam List<LocalTime> endTimes, @RequestParam List<Long> categories){
        return SuccessResponse.ok(reserveCommandService.findTodayReservation(date,firstAddress,secondAddress,thirdAddress,startTimes,endTimes,categories));
    }
    @GetMapping("/today/discount/{today}")
    public ResponseEntity<SuccessResponse<?>> findTodayReservationByDiscount
            (@PathVariable("today")LocalDate date,@RequestParam List<String> firstAddress,@RequestParam List<String> secondAddress,@RequestParam List<String> thirdAddress, @RequestParam List<LocalTime> startTimes,@RequestParam List<LocalTime> endTimes, @RequestParam List<Long> categories){
        return SuccessResponse.ok(reserveCommandService.findTodayReservationByDiscount(date,firstAddress,secondAddress,thirdAddress,startTimes,endTimes,categories));
    }
    /**
     * 마이페이지 전체 예약 조회
     */
    @GetMapping("/all")
    public ResponseEntity<SuccessResponse<?>> getAllReservationRecords(@UserId Long userId){
        return SuccessResponse.ok(reserveCommandService.getAllReservationRecords(userId));
    }
    /**
     * 마이페이지 전체 예약 조회- 카테고리별
     */
    @GetMapping("/all/categories/{categoryId}")
    public ResponseEntity<SuccessResponse<?>> getAllReservationRecordsByCategory(@UserId Long userId,@PathVariable("categoryId")Long categoryId){
        return SuccessResponse.ok(reserveCommandService.getAllReservationRecordsByCategory(userId,categoryId));
    }
    /**
     * 마이페이지 현재 예약 조회
     */
    @GetMapping("/now")
    public ResponseEntity<SuccessResponse<?>> getNowReservationRecords(@UserId Long userId){
        return SuccessResponse.ok(reserveCommandService.getNowReservationRecords(userId));
    }
    @GetMapping("/now/categories/{categoryId}")
    public ResponseEntity<SuccessResponse<?>> getNowReservationRecordsByCategory(@UserId Long userId,@PathVariable("categoryId")Long categoryId){
        return SuccessResponse.ok(reserveCommandService.getNowReservationRecordsByCategory(userId,categoryId));
    }
    /**
     * 예약 취소 및 삭제
     */
    @GetMapping("/{reservationId}/cancel")
    public ResponseEntity<SuccessResponse<?>> cancelReservation(@PathVariable("reservationId")Long reservationId){
        return SuccessResponse.ok(reserveCommandService.cancelReservation(reservationId));
    }
    @GetMapping("/{reservationId}/delete")
    public ResponseEntity<SuccessResponse<?>> deleteReservation(@PathVariable("reservationId")Long reservationId){
        return SuccessResponse.ok(reserveCommandService.deleteReservation(reservationId));
    }
    /**
     * 예약 마감 
     */
    @GetMapping("/owner/{scheduleId}/close")
    public ResponseEntity<SuccessResponse<?>> closeReservationSchedule(@PathVariable("scheduleId") Long reservationScheduleId){
        return SuccessResponse.ok(reserveOwnerCommandService.closeOrOpenReservationSchedule(reservationScheduleId));
    }
    @GetMapping("/owner/{reservationId}/confirm")
    public ResponseEntity<SuccessResponse<?>> confirmReservationRequest(@PathVariable("reservationId")Long reservationId){
        return SuccessResponse.ok(reserveOwnerCommandService.confirmReservationRequest(reservationId));
    }
    @PostMapping("/owner/{reservationId}/refuse")
    public ResponseEntity<SuccessResponse<?>> refuseReservationRequest(@PathVariable("reservationId")Long reservationId,@RequestBody ReserveRequestDTO.refuseReasonRequestDTO requestDTO){
        return SuccessResponse.ok(reserveOwnerCommandService.refuseReservationRequest(reservationId,requestDTO));
    }

}
