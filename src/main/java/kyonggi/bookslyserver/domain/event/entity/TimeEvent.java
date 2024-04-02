package kyonggi.bookslyserver.domain.event.entity;

import jakarta.persistence.*;
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
public class TimeEvent extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private int discountRate;

    private boolean repetitionStatus;

    private boolean isWeeklyRepeat;

    private boolean isRecurringDays;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "timeEventSchdule_id")
    private TimeEventSchedule timeEventSchedule;

    @OneToMany(mappedBy = "timeEvent")
    private List<WeeklyTimeEventDay> weeklyTimeEventDays = new ArrayList<>();

    @OneToMany(mappedBy = "timeEvent")
    private List<EmployeeTimeEvent> employeeTimeEvents = new ArrayList<>();
}
