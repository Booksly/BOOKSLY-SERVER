package kyonggi.bookslyserver.domain.event.dto.request;

import jakarta.validation.constraints.NotNull;
import kyonggi.bookslyserver.domain.event.validation.annotation.TimeRange;
import org.hibernate.validator.constraints.Range;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;

public record CreateStringTimeEventsRequestDto(
        @NotNull Long shopId,
        @NotNull String title,
        @NotNull List<Long> employeeIds,
        @NotNull @TimeRange String startTime,
        @NotNull @TimeRange String endTime,
        @NotNull @Range(min = 1 ,max = 100) int discountRate,
        @NotNull List<Long> menus,
        @NotNull Boolean isRepeat,
        @NotNull Boolean isDayOfWeekRepeat, // 요일 반복 여부 ex. 월,화,수...
        @NotNull Boolean isDateRepeat, // 날짜 반복 여부 ex. 2024-10-10 ~ 2024-10-20
        List<DayOfWeek> dayOfWeeks,
        LocalDate startDate,
        LocalDate endDate
) {
}
