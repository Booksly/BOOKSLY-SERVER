package kyonggi.bookslyserver.domain.shop.entity.Employee;

import jakarta.persistence.*;

import kyonggi.bookslyserver.domain.shop.dto.request.employee.EmployeeWorkScheduleDto;
import kyonggi.bookslyserver.global.common.BaseTimeEntity;



import lombok.*;

import java.time.DayOfWeek;
import java.time.LocalTime;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class WorkSchedule extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(value = EnumType.STRING)
    private DayOfWeek dayOfWeek;

    private LocalTime startTime;

    private LocalTime endTime;

    private boolean isDayOff;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id")
    private Employee employee;

    public static WorkSchedule createEntity(DayOfWeek dayOfWeek, LocalTime startTime, LocalTime endTime, boolean isDayOff, Employee employee){
        return WorkSchedule.builder()
                .dayOfWeek(dayOfWeek)
                .startTime(startTime)
                .endTime(endTime)
                .isDayOff(isDayOff)
                .employee(employee)
                .build();
    }
}
