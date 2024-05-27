package kyonggi.bookslyserver.domain.event.dto.response;

import kyonggi.bookslyserver.domain.reservation.entity.ReservationSchedule;
import lombok.Builder;

import java.util.List;
import java.util.stream.Collectors;

@Builder
public record GetTodayTimeEventsResponseDto(
        List<GetTodayTimeEventResponseDto> timeEvents
) {
    public static GetTodayTimeEventsResponseDto of(List<ReservationSchedule> eventReservationSchedules) {
        return GetTodayTimeEventsResponseDto.builder()
                .timeEvents(eventReservationSchedules.stream().map(GetTodayTimeEventResponseDto::of).collect(Collectors.toList()))
                .build();
    }
}
