package kyonggi.bookslyserver.domain.reservation.converter;

import kyonggi.bookslyserver.domain.reservation.dto.ReserveRequestDTO;
import kyonggi.bookslyserver.domain.reservation.dto.ReserveResponseDTO;
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
                .notice(requestDTO.getNotice())
                .build();
    }
    public static ReservationSetting updateReservationSetting(ReserveRequestDTO.reserveSettingRequestDTO request, ReservationSetting reservationSetting){
        reservationSetting.setRegisterMin(request.getRegisterMin());
        reservationSetting.setRegisterHr(request.getRegisterHr());
        reservationSetting.setAutoConfirmation(request.isAuto());
        reservationSetting.setMaxCapacity(request.getMaxCapacity());
        reservationSetting.setReservationCycle(request.getCycle());
        reservationSetting.setNotice(request.getNotice());
        return reservationSetting;
    }
    public static ReserveResponseDTO.reservationSettingResultDTO toReservationSettingResultDTO(ReservationSetting reservationSetting){
        return ReserveResponseDTO.reservationSettingResultDTO.builder()
                .shopId(reservationSetting.getShop().getId())
                .reservationSettingId(reservationSetting.getId())
                .build();
    }
}
