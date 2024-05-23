package kyonggi.bookslyserver.domain.notice.entity;

import jakarta.persistence.*;
import kyonggi.bookslyserver.domain.notice.constant.NoticeType;
import kyonggi.bookslyserver.domain.reservation.entity.Reservation;
import kyonggi.bookslyserver.domain.user.entity.ShopOwner;
import kyonggi.bookslyserver.global.common.BaseTimeEntity;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ShopOwnerNotice extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private NoticeType noticeType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shopOwner_id")
    private ShopOwner shopOwner;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="reservation_id")
    private Reservation reservation;
}
