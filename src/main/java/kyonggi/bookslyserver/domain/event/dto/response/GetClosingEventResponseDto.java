package kyonggi.bookslyserver.domain.event.dto.response;

import kyonggi.bookslyserver.domain.event.entity.closeEvent.ClosingEvent;
import lombok.Builder;

import java.util.List;

@Builder
public record GetClosingEventResponseDto(
        Long id,
        String employee,
        String message,
        int discountRate
) {
    public static GetClosingEventResponseDto of(ClosingEvent closingEvent) {
        return GetClosingEventResponseDto.builder()
                .id(closingEvent.getId())
                .employee(closingEvent.getEmployee().getName())
                .message(closingEvent.getEventMessage())
                .discountRate(closingEvent.getDiscountRate())
                .build();
    }
}
