package kyonggi.bookslyserver.domain.shop.dto.response.employee;

import kyonggi.bookslyserver.domain.shop.entity.Employee.WorkSchedule;
import lombok.Builder;

import java.time.DayOfWeek;
import java.time.LocalTime;

@Builder
public record UpdateEmployeeWorkScheduleResponseDto(
        DayOfWeek dayOfWeek,
        LocalTime startTime,
        LocalTime endTime,
        Boolean isDayOff
) {
    public static UpdateEmployeeWorkScheduleResponseDto of(WorkSchedule workSchedule) {
        return UpdateEmployeeWorkScheduleResponseDto.builder()
                .dayOfWeek(workSchedule.getDayOfWeek())
                .startTime(workSchedule.getStartTime())
                .endTime(workSchedule.getEndTime())
                .isDayOff(workSchedule.isDayOff()).build();
    }
}
