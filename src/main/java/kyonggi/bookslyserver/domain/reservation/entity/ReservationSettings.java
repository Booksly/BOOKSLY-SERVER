package kyonggi.bookslyserver.domain.reservation.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ReservationSettings {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private int registerMin;
    @Column // 둘 중 하나는 무조건 nullable=false
    private int registerHr;
    @Column(columnDefinition = "tinyint(0)")
    private boolean isAutoConfirmation;

    @Column // 위 값이 true일 때만 nullable=false
    private int maxCapacity;
    @Column(nullable = false)
    private int reservationCycle;

    @Column(columnDefinition = "VARCHAR")
    private String notice;
}
