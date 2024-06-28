package kyonggi.bookslyserver.domain.reservation.entity;

import jakarta.persistence.*;

import kyonggi.bookslyserver.domain.shop.entity.Menu.Menu;


import kyonggi.bookslyserver.global.common.BaseTimeEntity;

import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ReservationMenu extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="menu_id")
    private Menu menu;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="reervation_id")
    private Reservation reservation;
}
