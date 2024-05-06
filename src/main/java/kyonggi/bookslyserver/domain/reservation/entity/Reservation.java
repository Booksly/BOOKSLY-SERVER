package kyonggi.bookslyserver.domain.reservation.entity;

import jakarta.persistence.*;
import kyonggi.bookslyserver.domain.user.entity.User;
import kyonggi.bookslyserver.global.common.BaseTimeEntity;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
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

    @Column
    private String eventTitle;

    @Column
    private String refuseReason;

    @ManyToOne
    @JoinColumn(name = "reservationSchedule_id")
    private ReservationSchedule reservationSchedule;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "reservation",cascade = CascadeType.ALL)
    private List<ReservationMenu> reservationMenus=new ArrayList<>();

}
