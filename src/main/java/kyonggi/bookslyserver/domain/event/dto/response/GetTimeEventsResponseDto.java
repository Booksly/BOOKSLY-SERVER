package kyonggi.bookslyserver.domain.event.dto.response;

import kyonggi.bookslyserver.domain.event.entity.timeEvent.TimeEvent;
import lombok.Builder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Builder
public record GetTimeEventsResponseDto(
        List<GetTimeEventResponseDto> timeEvents
) {
    public static GetTimeEventsResponseDto of(Optional<List<TimeEvent>> timeEvents) {
        return GetTimeEventsResponseDto.builder()
                .timeEvents(timeEvents.orElse(new ArrayList<>())
                        .stream().map(timeEvent -> GetTimeEventResponseDto.of(timeEvent))
                        .collect(Collectors.toList())).build();
    }
}
