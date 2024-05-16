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

    public static WorkSchedule createEntity(Employee employee, EmployeeWorkScheduleDto dto){
        return WorkSchedule
                .builder()
                .dayOfWeek(dto.dayOfWeek())
                .startTime(dto.startTime())
                .endTime(dto.endTime())
                .isDayOff(dto.isDayOff())
                .employee(employee)
                .build();
    }
}
