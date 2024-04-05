package kyonggi.bookslyserver.domain.event.entity.timeEvent;

import jakarta.persistence.*;
import kyonggi.bookslyserver.domain.event.entity.timeEvent.TimeEvent;
import kyonggi.bookslyserver.global.common.BaseTimeEntity;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class WeeklyTimeEventDay extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String repeatDay;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "timeEvent_id")
    private TimeEvent timeEvent;
}
