package kyonggi.bookslyserver.domain.reservation.controller;

import kyonggi.bookslyserver.domain.reservation.dto.ReserveRequestDTO;
import kyonggi.bookslyserver.domain.reservation.service.ReserveCommandService;
import kyonggi.bookslyserver.global.auth.principal.user.UserId;
import kyonggi.bookslyserver.global.common.SuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import retrofit2.http.PATCH;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/reserve")
public class ShopUserReserveController {
    private final ReserveCommandService reserveCommandService;

    /**
     * 직원별, 날짜별 가능한 시간대 조회
     */
    @GetMapping("/time")
    public ResponseEntity<SuccessResponse<?>> getAvailableReservationTimes(@RequestParam("employeeId")Long employeeId, @RequestParam("date")LocalDate date){
        return SuccessResponse.ok(reserveCommandService.getAvailableReservationTimes(employeeId,date));
    }

    @PostMapping("/")
    public ResponseEntity<SuccessResponse<?>> createReservation(@UserId Long userId, @RequestBody ReserveRequestDTO.reservationRequestDTO request){
        return SuccessResponse.created(reserveCommandService.createReservation(userId, request));
    }

    @GetMapping("/todayReservations/{today}")
    public ResponseEntity<SuccessResponse<?>> findTodayReservation
            (@PathVariable("today")LocalDate date, @RequestParam List<LocalTime> startTimes,@RequestParam List<LocalTime> endTimes, @RequestParam List<String> categories){
        return SuccessResponse.ok("ok");
    }
}
