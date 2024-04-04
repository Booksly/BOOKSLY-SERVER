package kyonggi.bookslyserver.domain.shop.entity;

import jakarta.persistence.*;
import kyonggi.bookslyserver.domain.reservation.entity.ReservationSettings;
import kyonggi.bookslyserver.domain.user.entity.ShopOwner;
import kyonggi.bookslyserver.global.common.BaseTimeEntity;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.util.List;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Shop extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shopOwner_id")
    private ShopOwner shopOwner;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "reservationSettings_id", referencedColumnName = "id")
    private ReservationSettings reservationSettings;

}
