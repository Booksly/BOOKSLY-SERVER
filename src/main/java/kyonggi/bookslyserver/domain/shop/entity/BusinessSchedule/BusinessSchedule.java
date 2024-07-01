package kyonggi.bookslyserver.domain.shop.entity.BusinessSchedule;

import jakarta.persistence.*;
import kotlinx.serialization.descriptors.PrimitiveKind;
import kyonggi.bookslyserver.domain.shop.entity.Shop.Shop;
import kyonggi.bookslyserver.global.common.BaseTimeEntity;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@ToString(of={""})
public class BusinessSchedule extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private DayName day;

    private String openAt;

    private String closeAt;

    private Boolean isHoliday;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="shop_id")
    private Shop shop;

    public void update(DayName day, String openAt, String closeAt, boolean isHoliday){
        if(day != null) {
            this.day = day;
        }
        if(openAt != null) {
            this.openAt = openAt;
        }
        if(closeAt != null) {
            this.closeAt = closeAt;
        }
        if(isHoliday != this.isHoliday) {
            this.isHoliday = isHoliday;
        }
    }

    //== 연관 관계 편의 메서드 ==//
    public void addShop(Shop shop) {
        this.shop = shop;
        shop.getBusinessSchedules().add(this);
    }

}
