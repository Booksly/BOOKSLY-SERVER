package kyonggi.bookslyserver.domain.event.entity;

import jakarta.persistence.*;
import kyonggi.bookslyserver.domain.shop.entity.Employee;
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
