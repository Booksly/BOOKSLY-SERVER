package kyonggi.bookslyserver.domain.shop.entity;

import jakarta.persistence.*;
import kyonggi.bookslyserver.domain.reservation.entity.ReservationSettings;
import kyonggi.bookslyserver.domain.user.entity.ShopOwner;
import kyonggi.bookslyserver.global.common.BaseTimeEntity;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@ToString(of={"name", "store_number", "description"})
public class Shop extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="shop_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shopOwner_id")
    private ShopOwner shopOwner;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="category_id")
    private Category category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="address_id")
    private Address address;

    private String name;

    private LocalDateTime updated_at;

    private LocalDateTime created_at;

    private int total_visitors;

    private int today_visitors;

    private String store_number;

    private String instagram_url;

    private String kakao_url;

    @Lob
    private String description;

    private String detail_address;

    private boolean closed;

    private String zip_code;

    private String street_address;
  
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "reservationSettings_id", referencedColumnName = "id")
    private ReservationSettings reservationSettings;


}
