package kyonggi.bookslyserver.domain.user.entity;

import jakarta.persistence.*;
import kyonggi.bookslyserver.domain.notice.entity.UserNotice;
import kyonggi.bookslyserver.domain.reservation.entity.Reservation;
import kyonggi.bookslyserver.domain.review.entity.Review;
import kyonggi.bookslyserver.domain.user.constant.Role;
import kyonggi.bookslyserver.global.common.BaseTimeEntity;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class User extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="login_id")
    private String loginId;
    private String email;

    @Enumerated(value = EnumType.STRING)
    private Role role;

    private String phoneNum;

    private String nickname;

    private String profileImgUrl;

    private boolean isVerified;

    @Column(columnDefinition = "tinyint(0) default 0")
    private boolean isKakaoNotiEnabled;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<FavoriteShop> favoriteShops = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.PERSIST)
    private List<Review> reviews = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.PERSIST)
    private List<Reservation> reservations = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<UserNotice> userNotices = new ArrayList<>();

    public void updateUserInfo(String nickname, String profileImageUrl) {
        this.nickname = nickname;
        this.profileImgUrl = profileImageUrl;
    }
}
