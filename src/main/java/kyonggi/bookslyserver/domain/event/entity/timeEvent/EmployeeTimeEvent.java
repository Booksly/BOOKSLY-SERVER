package kyonggi.bookslyserver.domain.event.entity.timeEvent;

import jakarta.persistence.*;

import kyonggi.bookslyserver.domain.event.entity.timeEvent.TimeEvent;

import kyonggi.bookslyserver.domain.shop.entity.Employee.Employee;

import kyonggi.bookslyserver.global.common.BaseTimeEntity;
import lombok.*;

import static jakarta.persistence.FetchType.LAZY;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class EmployeeTimeEvent extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "timeEvent_id")
    private TimeEvent timeEvent;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "employee_id")
    private Employee employee;

    //== 연관관계 편의 메서드 ==//
    public void addTimeEvent(TimeEvent timeEvent) {
        this.timeEvent = timeEvent;
        timeEvent.getEmployeeTimeEvents().add(this);
    }

    public void addEmployee(Employee employee) {
        this.employee = employee;
        employee.getEmployeeTimeEvents().add(this);
    }
}
