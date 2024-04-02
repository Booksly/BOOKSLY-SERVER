package kyonggi.bookslyserver.domain.event.entity;

import jakarta.persistence.*;
import kyonggi.bookslyserver.global.common.BaseTimeEntity;
import lombok.*;

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

    private int eventYear;

    private int openMonth;

    private int openDay;

    private int openHour;

    private int openMin;

    private int closeMonth;

    private int closeDay;

    private int closeHour;

    private int closeMin;

    @OneToOne(fetch = FetchType.LAZY)
    private TimeEvent timeEvent;
}
