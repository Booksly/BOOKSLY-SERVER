package kyonggi.bookslyserver.domain.event.dto.response;

import kyonggi.bookslyserver.domain.reservation.entity.ReservationSchedule;
import lombok.Builder;

@Builder
public record ApplyClosingEventsResponseDto(
        Long reservationScheduleId,
        boolean isClosingEvent
) {
    public static ApplyClosingEventsResponseDto of(ReservationSchedule schedule) {
        return ApplyClosingEventsResponseDto.builder()
                .reservationScheduleId(schedule.getId())
                .isClosingEvent(schedule.isClosingEvent()).build();
    }
}
