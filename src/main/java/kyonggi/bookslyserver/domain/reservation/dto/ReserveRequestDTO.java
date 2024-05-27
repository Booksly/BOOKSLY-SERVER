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
        private Long empMenuId;
    }
    @Builder
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class reservationRequestDTO{

        private Long reservationScheduleId;
        private boolean event;
        private String eventTitle;
        private Integer discount;
        private String inquiry;
        /**
         * 직원메뉴 아이디 리스트
         */
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
