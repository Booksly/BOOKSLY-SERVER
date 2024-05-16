package kyonggi.bookslyserver.domain.event.dto.response;

import kyonggi.bookslyserver.domain.event.entity.closeEvent.ClosingEvent;
import lombok.Builder;

@Builder
public record CreateClosingEventResponseDto(
        Long eventId
) {
    public static CreateClosingEventResponseDto of(ClosingEvent closingEvent) {
        return CreateClosingEventResponseDto.builder().eventId(closingEvent.getId()).build();
    }
}
