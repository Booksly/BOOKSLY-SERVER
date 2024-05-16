package kyonggi.bookslyserver.domain.shop.dto.response.employee;

import kyonggi.bookslyserver.domain.shop.entity.BusinessSchedule.DayName;
import kyonggi.bookslyserver.domain.shop.entity.Employee.WorkSchedule;
import lombok.Data;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
public class EmployeeReadOneWorkScheduleDto {

    private DayOfWeek day;

    private LocalTime startTime;

    private LocalTime endTime;

    private Boolean isDayOff;

    public EmployeeReadOneWorkScheduleDto(WorkSchedule workSchedule){
        this.day = workSchedule.getDayOfWeek();
        this.startTime = workSchedule.getStartTime();
        this.endTime = workSchedule.getEndTime();
        this.isDayOff = workSchedule.isDayOff();
    }
}
