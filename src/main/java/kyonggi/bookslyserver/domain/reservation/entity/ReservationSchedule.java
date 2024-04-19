package kyonggi.bookslyserver.domain.reservation.entity;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;

import kyonggi.bookslyserver.domain.event.entity.closeEvent.ClosingEvent;
import kyonggi.bookslyserver.domain.shop.entity.Employee.Employee;
import kyonggi.bookslyserver.domain.shop.entity.Shop.Shop;
import kyonggi.bookslyserver.global.common.BaseTimeEntity;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

import static jakarta.persistence.FetchType.LAZY;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ReservationSchedule extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalTime startTime;

    @Column(nullable = false)
    private LocalTime endTime;

    @Column(columnDefinition = "tinyint(0) default 0")
    private boolean isClosed;

    @Column(columnDefinition = "tinyint(0) default 0")
    private boolean isClosingEvent;

    private LocalDate workDate;

    @Column(columnDefinition = "INT DEFAULT 0")
    private Integer reservedCapacity;

    @Column(nullable=false, columnDefinition = "tinyint(0)")
    private boolean isAutoConfirmed;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "closingEvent_id")
    private ClosingEvent closingEvent;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "shop_id")
    private Shop shop;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "employee_id")
    private Employee employee;
}
