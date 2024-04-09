package kyonggi.bookslyserver.domain.shop.entity.BusinessSchedule;

import jakarta.persistence.*;
import kyonggi.bookslyserver.domain.shop.entity.Shop.Shop;
import kyonggi.bookslyserver.global.common.BaseTimeEntity;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(of={""})
public class businessSchedule extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private DayName day;

    private String openAt;

    private String closeAt;

    private boolean isholiday;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="id")
    private Shop shop;




/*    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="businessDays_id")
    private BusinessDays businessdays;

    @ManyToOne
    @JoinColumn(name="id")
    private Shop shop;*/


}
