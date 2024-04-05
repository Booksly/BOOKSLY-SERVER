package kyonggi.bookslyserver.domain.reservation.entity;

import jakarta.persistence.*;
import kyonggi.bookslyserver.domain.shop.entity.Employee.Employee;
import kyonggi.bookslyserver.domain.shop.entity.Shop.Shop;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class EmployeeReservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "tinyint(0) default 0")
    private boolean isConfirmed;

    @Column(columnDefinition = "tinyint(0) default 0")
    private boolean isRefused;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="reservation_id")
    private Reservation reservation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="shop_id")
    private Shop shop;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="employee_id")
    private Employee employee;
}
