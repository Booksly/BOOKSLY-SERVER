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
    public static class getTodayReservationSchedulesResultDTO {
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
    public static class getOnlyReservationsOfDateResultDTO{
        private Long reservationScheduleId;
        private LocalTime reservationScheduleTime;
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
        private List<getOnlyReservationsOfDateResultDTO> reservationsList;
    }
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Setter
    @Getter
    public static class getTodayReservationsDetailsResultDTO{
        private Long reservationId;
        private LocalTime reservationScheduleTime;
        private List<String> reservationMenus;
        private String inquiry;
    }
    @Builder
    @Getter
    @Setter
    public static class getReservationsDetailsOfDateResultDTO{
        private String employeeName;
        private LocalDate date;
        private List<getTodayReservationsDetailsResultDTO> reservationList;
    }
    @Builder
    @Getter
    @Setter
    public static class getReservationScheduleOfDateResultDTO{
        private String employeeName;
        private LocalDate date;
        private List<getTodayReservationSchedulesResultDTO> scheduleList;
    }
}
