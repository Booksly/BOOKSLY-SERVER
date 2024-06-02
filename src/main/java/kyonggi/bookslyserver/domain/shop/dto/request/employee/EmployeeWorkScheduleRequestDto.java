package kyonggi.bookslyserver.domain.shop.dto.request.employee;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Builder;

import java.time.DayOfWeek;
import java.time.LocalTime;

@Builder
public record EmployeeWorkScheduleRequestDto(
        DayOfWeek dayOfWeek,
        LocalTime startTime,
        LocalTime endTime,
        Boolean isDayOff
) {
}
