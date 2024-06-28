package kyonggi.bookslyserver.domain.event.entity.timeEvent;

import jakarta.persistence.*;

import kyonggi.bookslyserver.domain.event.entity.timeEvent.TimeEvent;

import kyonggi.bookslyserver.domain.shop.entity.Menu.Menu;
import kyonggi.bookslyserver.global.common.BaseTimeEntity;
import lombok.*;
import org.hibernate.annotations.Fetch;

import static jakarta.persistence.FetchType.LAZY;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class TimeEventMenu extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //@ManyToOne(fetch = LAZY)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_id")
    private Menu menu;

    //@ManyToOne(fetch = LAZY)
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "timeEvent_id")
    private TimeEvent timeEvent;

    //== 연관관계 편의 메소드 ==//
    public void addMenu(Menu menu) {
        this.menu = menu;
        menu.getTimeEventMenus().add(this);
    }

    public void addTimeEvent(TimeEvent timeEvent) {
        this.timeEvent = timeEvent;
        timeEvent.getTimeEventMenus().add(this);
    }
}
