package kyonggi.bookslyserver.domain.reservation.dto;

import lombok.Builder;

public class ReserveResponseDTO {
    @Builder
    public static class reservationSettingResultDTO{
        private Long shopId;
        private Long reservationSettingId;
    }
}
