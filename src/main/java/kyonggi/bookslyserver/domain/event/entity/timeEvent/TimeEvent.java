package kyonggi.bookslyserver.domain.event.entity.timeEvent;

import jakarta.persistence.*;
import kyonggi.bookslyserver.domain.shop.entity.Shop.Shop;
import kyonggi.bookslyserver.global.common.BaseTimeEntity;
import lombok.*;

import java.time.DayOfWeek;
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
public class TimeEvent extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private int discountRate;

    private boolean repetitionStatus;

    private boolean isDayOfWeekRepeat; // 요일 반복

    private boolean isDateRepeat; // 날짜 반복

    private LocalTime startTime;

    private LocalTime endTime;

    @ElementCollection(targetClass = DayOfWeek.class)
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "time_event_day_of_week", joinColumns = @JoinColumn(name = "time_event_id"))
    @Column(name = "day_of_week")
    private List<DayOfWeek> dayOfWeeks = new ArrayList<>();

    @OneToMany(mappedBy = "timeEvent", cascade = CascadeType.ALL)
    @Builder.Default
    private List<EmployeeTimeEvent> employeeTimeEvents = new ArrayList<>();

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "shop_id")
    private Shop shop;

    @OneToMany(mappedBy = "timeEvent", cascade = CascadeType.ALL)
    @Builder.Default
    private List<TimeEventMenu> timeEventMenus = new ArrayList<>();

    @OneToMany(mappedBy = "timeEvent", cascade = CascadeType.ALL)
    @Builder.Default
    private List<TimeEventSchedule> timeEventSchedules = new ArrayList<>();

}
