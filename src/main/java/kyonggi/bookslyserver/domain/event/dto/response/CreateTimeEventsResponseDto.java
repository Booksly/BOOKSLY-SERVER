package kyonggi.bookslyserver.domain.event.dto.response;

import kyonggi.bookslyserver.domain.event.entity.timeEvent.TimeEvent;
import lombok.Builder;

@Builder
public record CreateTimeEventsResponseDto(
        Long timeEventId
) {
    public static CreateTimeEventsResponseDto of(TimeEvent timeEvent) {
        return CreateTimeEventsResponseDto.builder().timeEventId(timeEvent.getId()).build();
    }
}
