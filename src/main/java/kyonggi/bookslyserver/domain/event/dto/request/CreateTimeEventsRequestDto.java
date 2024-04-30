package kyonggi.bookslyserver.domain.event.dto.request;

import jakarta.validation.constraints.NotNull;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public record CreateTimeEventsRequestDto(
        @NotNull String title,
        @NotNull List<Long> employeeIds,
        @NotNull LocalTime startTime,
        @NotNull LocalTime endTime,
        @NotNull int discountRate,
        @NotNull List<Long> menus,
        @NotNull boolean isRepeat,
        @NotNull boolean isDayRepeat, // 요일 반복 여부 ex. 월,화,수...
        @NotNull boolean isDateRepeat, // 날짜 반복 여부 ex. 2024-10-10 ~ 2024-10-20
        List<DayOfWeek> days,
        LocalDate startDate,
        LocalDate endDate
) {
}
