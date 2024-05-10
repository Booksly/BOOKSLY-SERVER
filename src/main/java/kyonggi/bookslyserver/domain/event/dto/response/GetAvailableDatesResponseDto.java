package kyonggi.bookslyserver.domain.event.dto.response;

import lombok.Builder;

import java.time.LocalDate;
import java.util.List;

@Builder
public record GetAvailableDatesResponseDto(
        List<LocalDate> dates
) {
    public static GetAvailableDatesResponseDto of(List<LocalDate> workdays) {
        return GetAvailableDatesResponseDto.builder().dates(workdays).build();
    }
}
