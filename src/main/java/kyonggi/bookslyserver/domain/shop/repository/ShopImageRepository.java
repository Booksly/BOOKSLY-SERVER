package kyonggi.bookslyserver.domain.shop.repository;

import kyonggi.bookslyserver.domain.shop.entity.Shop.ShopImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShopImageRepository extends JpaRepository<ShopImage, Long> {
}
