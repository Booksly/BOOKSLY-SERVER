package kyonggi.bookslyserver.domain.reservation.entity;

import jakarta.persistence.*;
import kyonggi.bookslyserver.domain.review.entity.Review;
import kyonggi.bookslyserver.domain.user.entity.User;
import kyonggi.bookslyserver.global.common.BaseTimeEntity;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.FetchType.LAZY;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Reservation extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private int price;

    @Column
    private String inquiry;

    @Column(columnDefinition = "tinyint(0) default 0")
    private boolean isCanceled;
    
    @Column(columnDefinition = "tinyint(0) default 0")
    private boolean isConfirmed;

    @Column(columnDefinition = "tinyint(0) default 0")
    private boolean isRefused;

    @Column(columnDefinition = "tinyint(0) default 0")
    private boolean isDeleted;

    @Column
    private String timeEventTitle;

    @Column
    private String refuseReason;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "reservationSchedule_id")
    private ReservationSchedule reservationSchedule;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "reservation",cascade = CascadeType.ALL)
    private List<ReservationMenu> reservationMenus=new ArrayList<>();

    @OneToOne(fetch = LAZY)
    private Review review;

    public void deleteReview() {
        if (this.review != null) {
            this.review = null;
        }
    }

    public void setCanceled(boolean canceled) {
        isCanceled = canceled;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }
}
