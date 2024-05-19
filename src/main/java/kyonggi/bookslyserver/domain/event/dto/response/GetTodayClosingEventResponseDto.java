package kyonggi.bookslyserver.domain.event.dto.response;

import java.time.LocalTime;

public record GetTodayClosingEventResponseDto(
        Long shopId,
        String shopName,
        String storeImg,
        float rating,
        LocalTime time,
        String message
) {
    public static GetTodayClosingEventResponseDto of(){return null;}
}
