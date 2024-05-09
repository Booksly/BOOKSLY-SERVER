package kyonggi.bookslyserver.domain.event.dto.response;

import kyonggi.bookslyserver.domain.event.entity.closeEvent.ClosingEvent;
import lombok.Builder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Builder
public record GetClosingEventsResponseDto(
        List<GetClosingEventResponseDto> closingEventConfigs
) {
    public static GetClosingEventsResponseDto of(Optional<List<ClosingEvent>> closingEvents) {
        return GetClosingEventsResponseDto.builder()
                .closingEventConfigs(closingEvents.orElse(new ArrayList<>())
                        .stream().map(closingEvent -> GetClosingEventResponseDto.of(closingEvent))
                        .collect(Collectors.toList())).build();}
}
