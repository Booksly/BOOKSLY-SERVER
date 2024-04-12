package kyonggi.bookslyserver.domain.reservation.repository;

import kyonggi.bookslyserver.domain.reservation.entity.ReservationSetting;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservationSettingRepository extends JpaRepository<ReservationSetting,Long> {
}
