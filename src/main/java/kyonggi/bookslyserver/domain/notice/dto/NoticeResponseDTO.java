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
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Setter
    @Getter
    public static class confirmedReservationsResultDTO{
        private LocalDateTime createdTime;
        private String shopName;
        private String reservationTime;
        @Builder.Default
        private String staticWords="예약이 확정되었습니다";
    }
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Setter
    @Getter
    public static class todoReservationsResultDTO{
        private String employeeName;
        private String reservationTime;
    }
}
