package kyonggi.bookslyserver.domain.reservation.converter;

import kyonggi.bookslyserver.domain.reservation.dto.ReserveRequestDTO;
import kyonggi.bookslyserver.domain.reservation.entity.ReservationSetting;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ReservationConverter {
    public static ReservationSetting toReservationSetting(ReserveRequestDTO.reserveSettingRequestDTO requestDTO){
        return ReservationSetting.builder()
                .registerMin(requestDTO.getRegisterMin())
                .registerHr(requestDTO.getRegisterHr())
                .reservationCycle(requestDTO.getCycle())
                .maxCapacity(requestDTO.getMaxCapacity())
                .isAutoConfirmation(requestDTO.isAuto())
                .build();
    }
}
