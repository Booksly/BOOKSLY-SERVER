package kyonggi.bookslyserver.domain.reservation.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.time.LocalTime;

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
}
