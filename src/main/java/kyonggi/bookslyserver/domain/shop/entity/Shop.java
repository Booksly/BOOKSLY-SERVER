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
    private Long id;

    private String name;

    private int totalVisitors;

    private int todayVisitors;

    private String shopPhoneNumber;

    private String instagramUrl;

    private String kakaoUrl;

    @Lob
    private String introduction;

    private String detailAddress;

    private boolean isClosed;

    private String zipCode;

    private String streetAddress;
  
    @OneToOne(cascade = CascadeType.ALL,orphanRemoval = true)
    @JoinColumn(name = "reservationSettings_id", referencedColumnName = "id")
    private ReservationSettings reservationSettings;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shopOwner_id")
    private ShopOwner shopOwner;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="category_id")
    private Category category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="address_id")
    private Address address;

    /**
     * 1. 영업 일정 테이블 연관관계 매핑 필드 추가
     * 2. 가게 이미지 테이블 필드 추가
     * 3. 리뷰 테이블 필드 추가
     *
     */

}
