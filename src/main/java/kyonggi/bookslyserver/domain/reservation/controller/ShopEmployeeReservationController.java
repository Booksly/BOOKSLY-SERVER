package kyonggi.bookslyserver.domain.reservation.controller;

import kyonggi.bookslyserver.domain.reservation.service.ReserveOwnerCommandService;
import kyonggi.bookslyserver.global.common.SuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/employees/{employeeId}/reservations")
public class ShopEmployeeReservationController {
    private final ReserveOwnerCommandService reserveOwnerCommandService;
    /**
     * 개별 직원용 예약 및 일정 조회
     */
    @GetMapping("/{date}")
    public ResponseEntity<SuccessResponse<?>> getTodayReservationSchedules(@PathVariable("date") LocalDate today, @PathVariable("employeeId")Long employeeId){
        return SuccessResponse.ok(reserveOwnerCommandService.getTodayReservationSchedules(today, employeeId));
    }
    @GetMapping("/of-date/{date}")
    public ResponseEntity<SuccessResponse<?>> getTodayReservationsOnly(@PathVariable("date") LocalDate date, @PathVariable("employeeId")Long employeeId){
        return SuccessResponse.ok(reserveOwnerCommandService.getTodayReservationsOnly(date, employeeId));
    }
    /**
     * 예약 확인 페이지
     */
    @GetMapping("/details/{date}")
    public ResponseEntity<SuccessResponse<?>> getTodayReservationsDetails(@PathVariable("date")LocalDate today,@PathVariable("employeeId")Long employeeId){
        return SuccessResponse.ok(reserveOwnerCommandService.getTodayReservationsDetails(today, employeeId));
    }
    @GetMapping("/details/of-date/{date}")
    public ResponseEntity<SuccessResponse<?>> getReservationDetailsOfDate(@PathVariable("date")LocalDate date,@PathVariable("employeeId")Long employeeId){
        return SuccessResponse.ok(reserveOwnerCommandService.getReservationsOfDateDetails(date, employeeId));
    }
    @GetMapping("/schedules/{date}")
    public ResponseEntity<SuccessResponse<?>> getReservationScheduleOfDate(@PathVariable("date")LocalDate date,@PathVariable("employeeId")Long employeeId){
        return SuccessResponse.ok(reserveOwnerCommandService.getReservationScheduleOfDate(date, employeeId));
    }
    /**
     * 직원별 예약 일정 생성
     * 임시 uri api 테스트 시에만 사용 바람
     */
    @GetMapping("/schedules/create")
    public ResponseEntity<SuccessResponse<?>> createReservationSchedule(@PathVariable("employeeId")Long employeeId){
        return SuccessResponse.created(reserveOwnerCommandService.createEmployeeReservationSchedule(employeeId));
    }
}
