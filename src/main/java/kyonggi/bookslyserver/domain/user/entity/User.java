package kyonggi.bookslyserver.domain.user.entity;

import jakarta.persistence.*;
import kyonggi.bookslyserver.domain.user.constant.Role;
import kyonggi.bookslyserver.global.common.BaseTimeEntity;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@DynamicUpdate
@DynamicInsert
public class User extends BaseTimeEntity {

    @Id
    @GeneratedValue
    private Long id;

    @Column(name="social_id")
    private String socialId;
    private String email;

    @Enumerated(value = EnumType.STRING)
    @Column(columnDefinition = "VARCHAR(10) DEFAULT 'ROLE_USER'")
    private Role role;

    private String phoneNum;

    private String nickname;

    private String profileImgUrl;

    public void updateUserInfo(String nickname, String profileImageUrl) {
        this.nickname = nickname;
        this.profileImgUrl = profileImageUrl;
    }
}
