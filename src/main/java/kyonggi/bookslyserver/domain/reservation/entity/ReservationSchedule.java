package kyonggi.bookslyserver.domain.reservation.entity;

import jakarta.persistence.*;

import kyonggi.bookslyserver.domain.event.entity.closeEvent.ClosingEvent;
import kyonggi.bookslyserver.domain.event.entity.timeEvent.EmployTimeEventSchedule;
import kyonggi.bookslyserver.domain.shop.entity.Employee.Employee;
import kyonggi.bookslyserver.domain.shop.entity.Shop.Shop;
import kyonggi.bookslyserver.global.common.BaseTimeEntity;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

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

    @Column(nullable = false, columnDefinition = "tinyint(0)")
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

    @OneToMany(mappedBy = "reservationSchedule", cascade = CascadeType.ALL)
    private List<Reservation> reservations = new ArrayList<>(); // 이 사이즈가 reservedCapacity의 역할을 하게 될거임

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "employTimeEventSchedule_id")
    private EmployTimeEventSchedule employTimeEventSchedule;

    //==연관 관계 편의 메서드==//
    public void addEmployeeTimeEventSchedule(EmployTimeEventSchedule employTimeEventSchedule) {
        this.employTimeEventSchedule = employTimeEventSchedule;
        employTimeEventSchedule.getReservationSchedules().add(this);
    }

    public void addClosingEvent(ClosingEvent closingEvent) {
        this.closingEvent = closingEvent;
        this.isClosingEvent = true;
    }

    public void cancelClosingEvent() {
        this.closingEvent = null;
        this.isClosingEvent = false;
    }
}
