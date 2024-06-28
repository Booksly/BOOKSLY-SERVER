package kyonggi.bookslyserver.domain.reservation.converter;

import kyonggi.bookslyserver.domain.reservation.dto.ReserveRequestDTO;
import kyonggi.bookslyserver.domain.reservation.dto.ReserveResponseDTO;
import kyonggi.bookslyserver.domain.reservation.entity.Reservation;
import kyonggi.bookslyserver.domain.reservation.entity.ReservationSchedule;
import kyonggi.bookslyserver.domain.reservation.entity.ReservationSetting;
import kyonggi.bookslyserver.domain.shop.entity.Employee.Employee;
import kyonggi.bookslyserver.domain.shop.entity.Shop.Shop;
import lombok.RequiredArgsConstructor;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

@RequiredArgsConstructor
public class ReservationConverter {
    public static ReservationSetting toReservationSetting(ReserveRequestDTO.reservationSettingRequestDTO requestDTO, Shop shop){
        return ReservationSetting.builder()
                .registerMin(requestDTO.getRegisterMin())
                .registerHr(requestDTO.getRegisterHr())
                .reservationCycle(requestDTO.getCycle())
                .maxCapacity(requestDTO.getMaxCapacity())
                .isAutoConfirmation(requestDTO.isAuto())
                .notice(requestDTO.getNotice())
                .shop(shop)
                .build();
    }
/*    public static ReservationSetting updateReservationSetting(ReserveRequestDTO.reservationSettingRequestDTO request, ReservationSetting reservationSetting){
        reservationSetting.update(request);
        return reservationSetting;
    }*/
    public static ReserveResponseDTO.reservationSettingResultDTO toReservationSettingResultDTO(ReservationSetting reservationSetting){
        return ReserveResponseDTO.reservationSettingResultDTO.builder()
                .shopId(reservationSetting.getShop().getId())
                .reservationSettingId(reservationSetting.getId())
                .build();
    }

    public static ReservationSchedule toReservationSchedule(LocalTime startTime, LocalDate finalDate, Duration interval, Employee employee, Shop shop,boolean isAutoConfirmed){
        return ReservationSchedule.builder()
                .startTime(startTime)
                .endTime(startTime.plus(interval))
                .workDate(finalDate)
                .isAutoConfirmed(isAutoConfirmed)
                .employee(employee)
                .shop(shop)
                .reservations(new ArrayList<>())
                .build();
    }
    public static ReserveResponseDTO.availableTimesResultDTO toAvailableTimesResultDTO(ReservationSchedule reservationSchedule){
        return ReserveResponseDTO.availableTimesResultDTO.builder()
                .reservationScheduleId(reservationSchedule.getId())
                .time(reservationSchedule.getStartTime())
                .isClosed(reservationSchedule.isClosed())
                .build();
    }
    public static ReserveResponseDTO.createReservationResultDTO toCreateReservationResultDTO(Reservation reservation){
        return ReserveResponseDTO.createReservationResultDTO.builder()
                .reservationId(reservation.getId())
                .build();
    }
}
