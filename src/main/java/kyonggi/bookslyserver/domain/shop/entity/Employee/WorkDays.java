package kyonggi.bookslyserver.domain.shop.entity.Employee;

import jakarta.persistence.*;
import kyonggi.bookslyserver.domain.shop.entity.Employee.Employee;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class WorkDays {
    // 근무날짜 테이블
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id")
    private Employee employee;
}
