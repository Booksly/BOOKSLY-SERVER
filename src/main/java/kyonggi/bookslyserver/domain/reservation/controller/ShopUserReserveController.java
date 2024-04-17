package kyonggi.bookslyserver.domain.reservation.controller;

import kyonggi.bookslyserver.domain.reservation.dto.ReserveRequestDTO;
import kyonggi.bookslyserver.domain.reservation.service.ReserveCommandService;
import kyonggi.bookslyserver.global.common.SuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequiredArgsConstructor
@RequestMapping("/reserve")
public class ShopUserReserveController {
    private final ReserveCommandService reserveCommandService;

    /*
     * 직원별, 날짜별 가능한 시간대 조회
     */
    @GetMapping("/time")
    public ResponseEntity<SuccessResponse<?>> getAvailableReservationTimes(@RequestParam("employeeId")Long employeeId, @RequestParam("date")LocalDate date){
        return SuccessResponse.ok(reserveCommandService.getAvailableReservationTimes(employeeId,date));
    }

}
