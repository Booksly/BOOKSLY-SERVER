package kyonggi.bookslyserver.domain.event.dto.response;

import kyonggi.bookslyserver.domain.reservation.entity.ReservationSchedule;
import kyonggi.bookslyserver.domain.shop.entity.Shop.Shop;
import lombok.Builder;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Builder
public record GetTodayClosingEventsResponseDto(
        List<GetTodayClosingEventResponseDto> closingEvents
) {
    public static GetTodayClosingEventsResponseDto of(List<ReservationSchedule> eventReservationSchedules){
        return GetTodayClosingEventsResponseDto.builder()
                .closingEvents(eventReservationSchedules.stream().map(GetTodayClosingEventResponseDto::of).collect(Collectors.toList()))
                .build();
        }
}
