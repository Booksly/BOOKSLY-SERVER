package kyonggi.bookslyserver.domain.event.dto.response;

import kyonggi.bookslyserver.domain.event.entity.timeEvent.TimeEvent;
import lombok.Builder;

import java.util.List;

@Builder
public record GetTimeEventsResponseDto(
        List<GetTimeEventResponseDto> timeEvents
) {
    public GetTimeEventsResponseDto of(TimeEvent timeEvent) {
        return GetTimeEventsResponseDto.builder()
                .timeEvents(GetTimeEventResponseDto.of(timeEvent)).build();
    }
}
