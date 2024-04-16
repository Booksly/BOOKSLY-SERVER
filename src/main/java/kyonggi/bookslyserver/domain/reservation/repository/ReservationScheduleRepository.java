package kyonggi.bookslyserver.domain.reservation.repository;

import kyonggi.bookslyserver.domain.reservation.entity.ReservationSchedule;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservationScheduleRepository extends JpaRepository<ReservationSchedule,Long> {
}
