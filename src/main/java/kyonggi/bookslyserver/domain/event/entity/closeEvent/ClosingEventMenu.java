package kyonggi.bookslyserver.domain.event.entity.closeEvent;

import jakarta.persistence.*;
<<<<<<< HEAD:src/main/java/kyonggi/bookslyserver/domain/event/entity/closeEvent/ClosingEventMenu.java
import kyonggi.bookslyserver.domain.event.entity.closeEvent.ClosingEvent;
import kyonggi.bookslyserver.domain.shop.entity.Menu;
=======
import kyonggi.bookslyserver.domain.shop.entity.Menu.Menu;
>>>>>>> feature/#20:src/main/java/kyonggi/bookslyserver/domain/event/entity/ClosingEventMenu.java
import kyonggi.bookslyserver.global.common.BaseTimeEntity;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ClosingEventMenu extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "closingEvent_id")
    private ClosingEvent closingEvent;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_id")
    private Menu menu;

}
