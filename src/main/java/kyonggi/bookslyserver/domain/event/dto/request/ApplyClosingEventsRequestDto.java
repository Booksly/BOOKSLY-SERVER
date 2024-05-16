package kyonggi.bookslyserver.domain.event.dto.request;

public record ApplyClosingEventsRequestDto(
        Long shopId,
        Long reservationScheduleId
) {
}
