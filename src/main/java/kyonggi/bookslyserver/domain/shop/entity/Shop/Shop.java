package kyonggi.bookslyserver.domain.shop.entity.Shop;

import jakarta.persistence.*;
import kyonggi.bookslyserver.domain.reservation.entity.ReservationSettings;
import kyonggi.bookslyserver.domain.user.entity.ShopOwner;
import kyonggi.bookslyserver.global.common.BaseTimeEntity;
import lombok.*;

import java.time.LocalDateTime;

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

    private LocalDateTime updatedAt;

    private LocalDateTime createdAt;

    private int totalVisitors;

    private int todayVisitors;

    private String storeNumber;

    private String instagramUrl;

    private String kakaoUrl;

    @Lob
    private String description;

    private String detailAddress;

    private boolean closed;

    private String zipCode;

    private String streetAddress;
  
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "reservationSettings_id", referencedColumnName = "id")
    private ReservationSettings reservationSettings;


}
