package kyonggi.bookslyserver.domain.notice.dto;

import lombok.*;

import java.time.LocalDateTime;

public class NoticeResponseDTO {

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static abstract class BaseReservationDTO {
        private Long noticeId;
        private LocalDateTime createdTime;
        private String shopName;
        private String reservationTime;
    }
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Setter
    @Getter
    public static class RefusedReservationsResultDTO extends BaseReservationDTO{
        private String refuseReason;
    }
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Setter
    @Getter
    public static class ConfirmedReservationsResultDTO extends BaseReservationDTO{
        @Builder.Default
        private String staticWords="예약이 확정되었습니다";
    }
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Setter
    @Getter
    public static class CanceledReservationsResultDTO extends BaseReservationDTO{
        @Builder.Default
        private String staticWords="고객이 예약을 취소했습니다";
    }
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Setter
    @Getter
    public static class ReservationRequestsResultDTO extends BaseReservationDTO{
        @Builder.Default
        private String staticWords="새로운 예약 요청 건이 있습니다";
    }
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Setter
    @Getter
    public static class TodoReservationsResultDTO {
        private String employeeName;
        private String reservationTime;
    }

}
