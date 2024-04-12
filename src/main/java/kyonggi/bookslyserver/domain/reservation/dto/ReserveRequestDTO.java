package kyonggi.bookslyserver.domain.reservation.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

public class ReserveRequestDTO {
    @Builder
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class reserveSettingRequestDTO{
        private Integer registerMin;
        private Integer registerHr;
        @NotNull
        private boolean auto;
        private Integer maxCapacity;
        @NotNull
        private int cycle;
        private String notice;
    }
}
