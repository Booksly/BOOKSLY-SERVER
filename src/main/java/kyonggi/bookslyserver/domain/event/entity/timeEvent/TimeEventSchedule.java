package kyonggi.bookslyserver.domain.event.entity.timeEvent;

import jakarta.persistence.*;
import kyonggi.bookslyserver.domain.reservation.entity.ReservationSchedule;
import kyonggi.bookslyserver.domain.shop.entity.Employee.Employee;
import kyonggi.bookslyserver.global.common.BaseTimeEntity;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.FetchType.LAZY;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class TimeEventSchedule extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime startEventDateTime;

    private LocalDateTime endEventDateTime;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "employee_id")
    private Employee employee;

    @OneToMany(mappedBy = "timeEventSchedule")
    @Builder.Default
    private List<ReservationSchedule> reservationSchedules = new ArrayList<>();

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "timeEvent_id")
    private TimeEvent timeEvent;

    //== 연관관계 편의 메서드 ==//
    public void addEmployee(Employee employee) {
        this.employee = employee;
        employee.getTimeEventSchedules().add(this);
    }

    public void addTimeEvent(TimeEvent timeEvent) {
        this.timeEvent = timeEvent;
        timeEvent.getTimeEventSchedules().add(this);
    }
}
