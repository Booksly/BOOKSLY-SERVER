package kyonggi.bookslyserver.domain.reservation.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

public class ReserveRequestDTO {
    @Builder
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class reservationSettingRequestDTO {
        private Integer registerMin;
        private Integer registerHr;
        @NotNull
        private boolean auto;
        private Integer maxCapacity;
        @NotNull
        private int cycle;
        private String notice;
    }
    @Builder
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class reservationMenuRequestDTO{
        @NotNull
        private boolean event;
        private Integer discount;
        @NotNull
        private Long menuId;
        @NotNull
        private int price;
    }
    @Builder
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class reservationRequestDTO{
        @NotNull
        private Long reservationScheduleId;
        private String timeEventTitle;
        private String inquiry;
        /**
         * 요청 메뉴 리스트
         */
        @NotNull
        private List<reservationMenuRequestDTO> reservationMenus;
    }
    @Builder
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class refuseReasonRequestDTO{
        private String refuseReason;
    }
}
