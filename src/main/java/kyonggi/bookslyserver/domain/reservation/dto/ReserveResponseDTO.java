package kyonggi.bookslyserver.domain.reservation.dto;

import lombok.*;

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
}
