package kyonggi.bookslyserver.domain.notice.dto;

import lombok.*;

import java.time.LocalDateTime;

public class NoticeResponseDTO {
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Setter
    @Getter
    public static class refusedReservationsResultDTO{
        private LocalDateTime createdTime;
        private String shopName;
        private String reservationTime;
        private String refuseReason;
    }
}
