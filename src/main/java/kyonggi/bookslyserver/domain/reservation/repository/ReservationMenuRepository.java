package kyonggi.bookslyserver.domain.reservation.repository;

import kyonggi.bookslyserver.domain.reservation.entity.ReservationMenu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReservationMenuRepository extends JpaRepository<ReservationMenu,Long> {
}
