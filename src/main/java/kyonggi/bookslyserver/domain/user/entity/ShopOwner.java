package kyonggi.bookslyserver.domain.user.entity;

import jakarta.persistence.*;
import kyonggi.bookslyserver.domain.notice.entity.ShopOwnerNotice;
import kyonggi.bookslyserver.domain.shop.entity.Shop.Shop;
import kyonggi.bookslyserver.domain.user.constant.Role;
import kyonggi.bookslyserver.domain.user.dto.request.JoinOwnerRequestDto;
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

    @OneToMany(mappedBy = "shopOwner", cascade = CascadeType.PERSIST)
    private List<Shop> shops = new ArrayList<>();

    //==생성 메서드==//
    public static ShopOwner createShopOwner(JoinOwnerRequestDto joinOwnerRequestDto) {
        User ownerUser = User.builder()
                .role(Role.ROLE_ADMIN)
                .isVerified(true)
                .loginId(joinOwnerRequestDto.loginId())
                .email(joinOwnerRequestDto.email())
                .isKakaoNotiEnabled(false)
                .phoneNum(joinOwnerRequestDto.phoneNum()).build();

        return ShopOwner.builder()
                .password(joinOwnerRequestDto.password())
                .businessNumber(joinOwnerRequestDto.businessNumber())
                .user(ownerUser).build();
    }
}
