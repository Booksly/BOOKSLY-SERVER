package kyonggi.bookslyserver.domain.shop.dto.request;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Builder;

import java.time.DayOfWeek;
import java.time.LocalTime;

@Builder
public record EmployeeWorkScheduleDto(
        @Enumerated(value = EnumType.STRING)
        DayOfWeek dayOfWeek,
        LocalTime startTime,
        LocalTime endTime,

        Boolean isDayOff
) {
}
