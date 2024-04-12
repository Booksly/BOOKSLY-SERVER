package kyonggi.bookslyserver.domain.shop.repository;

import kyonggi.bookslyserver.domain.shop.entity.Shop.Shop;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShopRepository extends JpaRepository<Shop,Long> {
}
