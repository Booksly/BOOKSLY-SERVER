package kyonggi.bookslyserver.domain.event.entity.timeEvent;

import jakarta.persistence.*;
<<<<<<< HEAD:src/main/java/kyonggi/bookslyserver/domain/event/entity/timeEvent/EmployeeTimeEvent.java
import kyonggi.bookslyserver.domain.event.entity.timeEvent.TimeEvent;
import kyonggi.bookslyserver.domain.shop.entity.Employee;
=======
import kyonggi.bookslyserver.domain.shop.entity.Employee.Employee;
>>>>>>> feature/#20:src/main/java/kyonggi/bookslyserver/domain/event/entity/EmployeeTimeEvent.java
import kyonggi.bookslyserver.global.common.BaseTimeEntity;
import lombok.*;

import static jakarta.persistence.FetchType.LAZY;

@Entity
@Getter
@Setter
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
}
