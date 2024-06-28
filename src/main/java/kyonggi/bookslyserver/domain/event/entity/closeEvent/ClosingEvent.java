package kyonggi.bookslyserver.domain.event.entity.closeEvent;

import jakarta.persistence.*;
import kyonggi.bookslyserver.domain.event.entity.Event;
import kyonggi.bookslyserver.domain.reservation.entity.ReservationSchedule;
import kyonggi.bookslyserver.domain.shop.entity.Employee.Employee;
import kyonggi.bookslyserver.global.common.BaseTimeEntity;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ClosingEvent extends BaseTimeEntity implements Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int discountRate;

    private String eventMessage;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id")
    private Employee employee;

    @OneToMany(mappedBy = "closingEvent", cascade = CascadeType.ALL)
    @Builder.Default
    private List<ClosingEventMenu> closingEventMenus = new ArrayList<>();

    @Override
    public String getTitle() {
        return this.eventMessage;
    }

    @Override
    public int getDiscountRate() {
        return this.discountRate;
    }
}
