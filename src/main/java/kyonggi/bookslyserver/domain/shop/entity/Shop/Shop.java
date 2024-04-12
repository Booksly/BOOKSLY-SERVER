package kyonggi.bookslyserver.domain.shop.entity.Shop;

import jakarta.persistence.*;
import kyonggi.bookslyserver.domain.reservation.entity.ReservationSchedule;
import kyonggi.bookslyserver.domain.reservation.entity.ReservationSetting;
import kyonggi.bookslyserver.domain.review.entity.Review;
import kyonggi.bookslyserver.domain.shop.entity.BusinessSchedule.BusinessSchedule;
import kyonggi.bookslyserver.domain.shop.entity.Menu.Menu;
import kyonggi.bookslyserver.domain.user.entity.ShopOwner;
import kyonggi.bookslyserver.global.common.BaseTimeEntity;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.FetchType.LAZY;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@ToString(of = {"name", "store_number", "description"})
public class Shop extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private int totalVisitors;

    private int todayVisitors;

    private String phoneNumber;

    private String instagramUrl;

    private String kakaoUrl;
    @Lob
    private String introduction;

    private String detailAddress;

    private boolean isClosed;

    private String zipCode;

    private String streetAddress;

    private String businessNumber;

//    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
//    @JoinColumn(name = "reservationSettings_id", referencedColumnName = "id")
//    private ReservationSetting reservationSetting;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "shopOwner_id")
    private ShopOwner shopOwner;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "address_id")
    private Address address;

    @OneToMany(mappedBy = "shop")
    private List<ShopImage> shopImages = new ArrayList<>();

    @OneToMany(mappedBy = "shop")
    private List<Review> reviews = new ArrayList<>();

    @OneToMany(mappedBy = "shop")
    private List<BusinessSchedule> businessSchedules = new ArrayList<>();

    @OneToMany(mappedBy = "shop")
    private List<Menu> menus = new ArrayList<>();

    @OneToMany(mappedBy = "shop")
    private List<ReservationSchedule> reservationSchedules = new ArrayList<>();
}
