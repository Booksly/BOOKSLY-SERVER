package kyonggi.bookslyserver.domain.event.entity.timeEvent;

import jakarta.persistence.*;
import kyonggi.bookslyserver.domain.event.entity.timeEvent.TimeEvent;
import kyonggi.bookslyserver.global.common.BaseTimeEntity;
import lombok.*;

import java.time.LocalDateTime;

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

    private LocalDateTime openEventDate;

    private LocalDateTime closeEventDate;

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "timeEventSchedule")
    private TimeEvent timeEvent;
}
