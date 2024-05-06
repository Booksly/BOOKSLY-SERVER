package kyonggi.bookslyserver.domain.event.dto.request;

import jakarta.validation.constraints.NotNull;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public record CreateTimeEventsRequestDto(
        @NotNull Long shopId,
        @NotNull String title,
        @NotNull List<Long> employeeIds,
        @NotNull LocalTime startTime, // 검증 필요: 영업 시작 시간 이후일 것
        @NotNull LocalTime endTime, // 검증 필요: 영업 마감 시간 이전일 것, startTime 이후일 것
        @NotNull int discountRate,
        @NotNull List<Long> menus,
        @NotNull Boolean isRepeat,
        @NotNull Boolean isDayOfWeekRepeat, // 요일 반복 여부 ex. 월,화,수...
        @NotNull Boolean isDateRepeat, // 날짜 반복 여부 ex. 2024-10-10 ~ 2024-10-20
        List<DayOfWeek> dayOfWeeks,
        LocalDate startDate,
        LocalDate endDate
) {
}
