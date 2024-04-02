package kyonggi.bookslyserver.domain.event.entity;

import jakarta.persistence.*;
import kyonggi.bookslyserver.domain.reservation.entity.ReservationSchedule;
import kyonggi.bookslyserver.domain.shop.entity.Employee;
import kyonggi.bookslyserver.global.common.BaseTimeEntity;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ClosingEvent extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int discountRate;

    private boolean isAutoConfirm;

    private String eventMessage;

    private int concurrentBookingLimit;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id")
    private Employee employee;

    @OneToMany(mappedBy = "closingEvent")
    private List<ClosingEventMenu> closingEventMenus = new ArrayList<>();

    @OneToMany(mappedBy = "closingEvent")
    private List<ReservationSchedule> reservationSchedules = new ArrayList<>();
}
