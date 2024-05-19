package kyonggi.bookslyserver.domain.event.dto.request;

import jakarta.validation.constraints.NotNull;
import kyonggi.bookslyserver.domain.event.validation.annotation.TimeRange;
import kyonggi.bookslyserver.global.error.ErrorCode;
import kyonggi.bookslyserver.global.error.exception.InvalidValueException;
import lombok.Builder;
import org.hibernate.validator.constraints.Range;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Builder
public record CreateLocalTimeEventsRequestDto(
        Long shopId,
        String title,
        List<Long> employeeIds,
        LocalTime startTime,
        LocalTime endTime,
        int discountRate,
        List<Long> menus,
        Boolean isRepeat,
        Boolean isDayOfWeekRepeat,
        Boolean isDateRepeat,
        List<DayOfWeek> dayOfWeeks,
        LocalDate startDate,
        LocalDate endDate
) {
    public static CreateLocalTimeEventsRequestDto of(CreateStringTimeEventsRequestDto stringTimeEventsRequestDto) {
        LocalTime startTime = LocalTime.parse(stringTimeEventsRequestDto.startTime());
        LocalTime endTime = LocalTime.parse(stringTimeEventsRequestDto.endTime());

        return CreateLocalTimeEventsRequestDto.builder()
                .shopId(stringTimeEventsRequestDto.shopId())
                .title(stringTimeEventsRequestDto.title())
                .employeeIds(stringTimeEventsRequestDto.employeeIds())
                .startTime(startTime)
                .endTime(endTime)
                .startDate(stringTimeEventsRequestDto.startDate())
                .endDate(stringTimeEventsRequestDto.endDate())
                .discountRate(stringTimeEventsRequestDto.discountRate())
                .menus(stringTimeEventsRequestDto.menus())
                .isRepeat(stringTimeEventsRequestDto.isRepeat())
                .isDayOfWeekRepeat(stringTimeEventsRequestDto.isDayOfWeekRepeat())
                .isDateRepeat(stringTimeEventsRequestDto.isDateRepeat())
                .dayOfWeeks(stringTimeEventsRequestDto.dayOfWeeks()).build();
    }
}
