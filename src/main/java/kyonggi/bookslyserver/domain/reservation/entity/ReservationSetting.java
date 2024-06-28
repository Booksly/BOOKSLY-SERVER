package kyonggi.bookslyserver.domain.reservation.entity;

import jakarta.persistence.*;
import kyonggi.bookslyserver.domain.shop.entity.Shop.Shop;
import kyonggi.bookslyserver.global.common.BaseTimeEntity;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ReservationSetting extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private Integer registerMin;
    @Column
    private Integer registerHr;
    @Column(columnDefinition = "tinyint(0) default 0")
    private boolean isAutoConfirmation;

    @Column
    private Integer maxCapacity;
    @Column(nullable = false)
    private int reservationCycle;

    @Column(columnDefinition = "VARCHAR(255)")
    private String notice;

    @OneToOne
    @JoinColumn(name = "shop_id",referencedColumnName = "id")
    private Shop shop;
}
