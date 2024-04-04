package kyonggi.bookslyserver.domain.reservation.entity;

import jakarta.persistence.*;
import kyonggi.bookslyserver.domain.user.entity.User;
import kyonggi.bookslyserver.global.common.BaseTimeEntity;
import lombok.*;

import java.time.LocalDateTime;

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
    private LocalDateTime reserveDate;

    @Column(nullable = false)
    private LocalDateTime reserveTime;

    @Column(nullable = false)
    private Long price;

    @Column
    private String request;

    @Column(columnDefinition = "tinyint(0) default 0")
    private boolean isCanceled;

//    @Column(columnDefinition = "tinyint(0) default 0")
//    private boolean isClosed;

//    @Column(nullable = false)
//    @Enumerated(EnumType.STRING)

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
}
