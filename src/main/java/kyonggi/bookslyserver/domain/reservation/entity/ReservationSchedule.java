package kyonggi.bookslyserver.domain.reservation.entity;

import jakarta.persistence.*;
import kyonggi.bookslyserver.domain.event.entity.ClosingEvent;
import kyonggi.bookslyserver.domain.shop.entity.Shop;
import kyonggi.bookslyserver.domain.shop.entity.WorkDays;
import kyonggi.bookslyserver.global.common.BaseTimeEntity;
import lombok.*;

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
    private int startTime;
    @Column(nullable = false)
    private int endTime;

    @Column(columnDefinition = "tinyint(0) default 0")
    private boolean isClosed;

    @Column(columnDefinition = "tinyint(0) default 0")
    private boolean isClosingEvent;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "closingEvent_id")
    private ClosingEvent closingEvent;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shop_id")
    private Shop shop;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "workDays_id")
    private WorkDays workDays;

}
