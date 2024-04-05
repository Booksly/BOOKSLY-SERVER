package kyonggi.bookslyserver.domain.user.entity;

import jakarta.persistence.*;
import kyonggi.bookslyserver.domain.notice.entity.ShopOwnerNotice;
import kyonggi.bookslyserver.domain.shop.entity.Shop;
import kyonggi.bookslyserver.domain.user.constant.Role;
import kyonggi.bookslyserver.global.common.BaseTimeEntity;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter
public class ShopOwner extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String password;

    private String businessNumber;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "shopOwner", cascade = CascadeType.ALL)
    private List<ShopOwnerNotice> ownerNotices = new ArrayList<>();

    @OneToMany(mappedBy = "shopOwner", cascade = CascadeType.ALL) //persist
    private List<Shop> shops = new ArrayList<>();

    //==생성 메서드==//
    public static ShopOwner createShopOwner(String loginId, String password, String email, String phoneNum, String profileImgUrl) {

        User shopUser = User.builder()
                .role(Role.ROLE_ADMIN)
                .isVerified(true)
                .loginId(loginId)
                .email(email)
                .phoneNum(phoneNum)
                .profileImgUrl(profileImgUrl).build();

        return ShopOwner.builder()
                .password(password)
                .user(shopUser).build();
    }
}
