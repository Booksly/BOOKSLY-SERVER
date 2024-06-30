package kyonggi.bookslyserver.domain.shop.entity.Shop;

import jakarta.persistence.*;
import kyonggi.bookslyserver.domain.event.entity.timeEvent.TimeEvent;
import kyonggi.bookslyserver.domain.reservation.entity.ReservationSchedule;
import kyonggi.bookslyserver.domain.review.entity.Review;
import kyonggi.bookslyserver.domain.shop.dto.request.shop.ShopCreateRequestDto;
import kyonggi.bookslyserver.domain.shop.dto.request.shop.ShopUpdateRequestDto;
import kyonggi.bookslyserver.domain.shop.entity.BusinessSchedule.BusinessSchedule;
import kyonggi.bookslyserver.domain.shop.entity.Employee.Employee;
import kyonggi.bookslyserver.domain.shop.entity.Menu.Menu;
import kyonggi.bookslyserver.domain.shop.entity.Menu.MenuCategory;
import kyonggi.bookslyserver.domain.user.entity.FavoriteShop;
import kyonggi.bookslyserver.domain.user.entity.ShopOwner;
import kyonggi.bookslyserver.global.common.BaseTimeEntity;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static jakarta.persistence.FetchType.LAZY;

@Entity
@Getter
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

    private String blogUrl;

    @Lob
    private String introduction;

    private Long zipCode;

    private String streetAddress;

    private String detailAddress;

    private String businessNumber;

    @Column(columnDefinition = "tinyint(0) default 0")
    private Boolean isRepresentative;

    @Column(columnDefinition = "tinyint(0) default 0")
    private Boolean isDeleted;

    private LocalDateTime deletedAt;
    @Embedded
    private TimeUnit timeUnit;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "shopOwner_id")
    private ShopOwner shopOwner;

    @ManyToOne(fetch = LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne(fetch = LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "address_id")
    private Address address;

    @OneToMany(mappedBy = "shop", cascade = CascadeType.ALL)
    private List<MenuCategory> menuCategories = new ArrayList<>();

    @OneToMany(mappedBy = "shop", cascade = CascadeType.ALL)
    private List<ShopImage> shopImages = new ArrayList<>();

    @OneToMany(mappedBy = "shop", cascade = CascadeType.ALL)
    private List<Review> reviews = new ArrayList<>();

    @OneToMany(mappedBy = "shop", cascade = CascadeType.ALL)
    private List<BusinessSchedule> businessSchedules = new ArrayList<>();

    @OneToMany(mappedBy = "shop", cascade = CascadeType.ALL)
    private List<Menu> menus = new ArrayList<>();

    @OneToMany(mappedBy = "shop", cascade = CascadeType.ALL)
    private List<ReservationSchedule> reservationSchedules = new ArrayList<>();

    @OneToMany(mappedBy = "shop", cascade = CascadeType.ALL)
    private List<Employee> employees = new ArrayList<>();

    @OneToMany(mappedBy = "shop", cascade = CascadeType.ALL)
    private List<FavoriteShop> favoriteShops = new ArrayList<>();

    @OneToMany(mappedBy = "shop", cascade = CascadeType.ALL)
    private List<TimeEvent> timeEvents = new ArrayList<>();

    public void setShopOwner(ShopOwner shopOwner){
        this.shopOwner = shopOwner;
        this.shopOwner.getShops().add(this);
    }

    public void addMenuCategory(MenuCategory menuCategory){
        this.menuCategories.add(menuCategory);
    }

    public static Shop createShop(ShopCreateRequestDto requestDto){

        Shop shop = Shop.builder()
                .name(requestDto.getName())
                .phoneNumber(requestDto.getPhoneNumber())
                .businessNumber(requestDto.getBusinessNumber())
                .address(Address.builder().firstAddress(requestDto.getFirstAddress()).secondAddress(requestDto.getSecondAddress()).thirdAddress(requestDto.getThirdAddress()).build())
                .detailAddress(requestDto.getDetailAddress())
                .zipCode(requestDto.getZipCode())
                .streetAddress(requestDto.getStreetAddress())
                .introduction(requestDto.getIntroduction())
                .businessSchedules(new ArrayList<>())
                .shopImages(new ArrayList<>())
                .timeUnit(requestDto.getTimeUnit())
                .build();

        String url=requestDto.getSnsUrl();
        shop.assignSNSUrl(url);
        return shop;
    }
    public void update(ShopUpdateRequestDto requestDto) {
        int business_flag = 0;
        int shopImage_flag = 0;

        Optional.ofNullable(requestDto.name()).ifPresent(name -> this.name = name);
        Optional.ofNullable(requestDto.phoneNumber()).ifPresent(phoneNumber -> this.phoneNumber = phoneNumber);

        this.address.update(
                requestDto.firstAddress(),
                requestDto.secondAddress(),
                requestDto.thirdAddress()
        );

        Optional.ofNullable(requestDto.detailAddress()).ifPresent(detailAddress -> this.detailAddress = detailAddress);
        Optional.ofNullable(requestDto.kakaoUrl()).ifPresent(kakaoUrl -> this.kakaoUrl = kakaoUrl);
        Optional.ofNullable(requestDto.instagramUrl()).ifPresent(instagramUrl -> this.instagramUrl = instagramUrl);
        Optional.ofNullable(requestDto.introduction()).ifPresent(introduction -> this.introduction = introduction);
        if (requestDto.businessScheduleList() != null) {
            for (BusinessSchedule businessSchedule : requestDto.businessScheduleList()) {
                if (business_flag < this.businessSchedules.size()) {
                    this.businessSchedules.get(business_flag)
                            .update(
                                    businessSchedule.getDay(),
                                    businessSchedule.getOpenAt(),
                                    businessSchedule.getCloseAt(),
                                    businessSchedule.getIsHoliday()
                            );
                }
                business_flag++;
            }
        }
        if (requestDto.shopImageList()!=null){
            for (ShopImage shopImage : requestDto.shopImageList()) {
                if (shopImage_flag < this.shopImages.size()) {
                    this.shopImages.get(shopImage_flag)
                            .update(
                                    shopImage.getImgUri(),
                                    shopImage.getIsRepresentative()
                            );
                }
                shopImage_flag++;
            }
        }
    }
    public float getRatingByReview() {
        if (reviews.size() > 0) {
            float sum = 0;
            for (Review review : reviews) {
                sum += review.getRating();
            }
            return sum / reviews.size();
        }
        return 0;
    }

    public void markAsDeleted() {
        this.isDeleted = true;
        this.deletedAt = LocalDateTime.now();
    }

    public void assignSNSUrl(String url) {
        final String kakao = "pf.kakao.com";
        final String instagram = "instagram.com";
        if (url.contains(kakao)){
            this.kakaoUrl = url;
        } else if (url.contains(instagram)) {
            this.instagramUrl = url;
        }else{
            this.blogUrl = url;
        }
    }

    // setter
    public void setCategory(Category category) {
        this.category = category;
    }

    public void setRepresentative(Boolean representative) {
        isRepresentative = representative;
    }

    public void setTotalVisitors(int totalVisitors) {
        this.totalVisitors = totalVisitors;
    }
}
