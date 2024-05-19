package kyonggi.bookslyserver.domain.event.dto.response;

import lombok.Builder;

import java.util.List;

@Builder
public record GetTodayClosingEventsResponseDto(
        List<GetTodayClosingEventResponseDto> closingEvents
) {
    public static GetTodayClosingEventResponseDto of(){
        return null;}
}
