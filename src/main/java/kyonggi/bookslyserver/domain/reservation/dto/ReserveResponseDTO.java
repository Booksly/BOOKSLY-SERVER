package kyonggi.bookslyserver.domain.reservation.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class ReserveResponseDTO {
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Setter
    @Getter
    public static class reservationSettingResultDTO{
        private Long shopId;
        private Long reservationSettingId;
    }
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Setter
    @Getter
    public static class availableTimesResultDTO{
        private Long reservationScheduleId;
        private LocalTime time;
        @JsonProperty("isClosed")
        private boolean isClosed;
    }
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Setter
    @Getter
    public static class createReservationResultDTO{
        private Long reservationId;
    }
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Setter
    @Getter
    public static class getReservationRequestResultDTO{
        private Long reservationId;
        private LocalDate reservationDate;
        private LocalTime reservationTime;
        private String employeeName;
    }

    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Setter
    @Getter
    public static class getDatesWithResReqResultDTO{
        private LocalDate reservationDate;
    }
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Setter
    @Getter
    public static class reservationMenu{
        private String menuName;
    }
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Setter
    @Getter
    public static class getTodayReservationsResultDTO{
        private Long reservationScheduleId;
        private LocalTime reservationScheduleTime;
        private boolean isClosed;
        private List<reservationMenu> reservationMenus;
    }
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Setter
    @Getter
    public static class getTodayReservationsAllEmpsResultDTO {
        private LocalTime reservationScheduleTime;
        private List<reservationMenu> reservationMenus;
    }
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Setter
    @Getter
    public static class getOnlyReservationsOfDateAllEmpsResultDTO{
        private String employeeName;
        private List<getTodayReservationsResultDTO> getTodayReservationsResultDTOS;
    }

    /**
     * 추가 할 일
     * 1. 현재 있는 dto는 예약 일정 조회하는 dto니까 예약만 조회하는 dto 따로 생성 (시간이랑 메뉴 리스트만 있도록)
     * 2. 새 dto 생성하면 그거로 대체하기
     */
}
