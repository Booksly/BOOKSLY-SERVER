package kyonggi.bookslyserver.domain.reservation.repository;

import kyonggi.bookslyserver.domain.reservation.entity.Reservation;
import kyonggi.bookslyserver.domain.reservation.repository.CustomRepository.ReservationRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation,Long>, ReservationRepositoryCustom {
}
