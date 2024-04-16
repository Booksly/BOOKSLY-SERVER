package kyonggi.bookslyserver.domain.reservation.repository;

import kyonggi.bookslyserver.domain.reservation.entity.ReservationSetting;
import kyonggi.bookslyserver.domain.shop.entity.Shop.Shop;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ReservationSettingRepository extends JpaRepository<ReservationSetting,Long> {
    Optional<ReservationSetting> findByShop(Shop shop);
}
