package kyonggi.bookslyserver.domain.event.dto.response;

import kyonggi.bookslyserver.domain.event.entity.timeEvent.TimeEvent;
import lombok.Builder;

@Builder
public record GetTimeEventResponseDto(
        Long id,
        String title,
        String startTime,
        String endTime
) {
    public static GetTimeEventResponseDto of(TimeEvent timeEvent) {
        return GetTimeEventResponseDto.builder()
                .id(timeEvent.getId())
                .title(timeEvent.getTitle())
                .endTime(String.valueOf(timeEvent.getEndTime()))
                .startTime(String.valueOf(timeEvent.getStartTime()))
                .build();
    }
}
