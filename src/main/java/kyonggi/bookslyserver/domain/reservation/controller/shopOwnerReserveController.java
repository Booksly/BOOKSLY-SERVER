package kyonggi.bookslyserver.domain.reservation.controller;

import kyonggi.bookslyserver.domain.reservation.dto.ReserveRequestDTO;
import kyonggi.bookslyserver.global.common.SuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/reserve")
public class shopOwnerReserveController {
    @PostMapping("/setting")
    public ResponseEntity<SuccessResponse<?>> setReservationSetting
            (@RequestParam("shopId")Long shopId, @ModelAttribute ReserveRequestDTO.reserveSettingRequestDTO request){


        return null;
    }
}
