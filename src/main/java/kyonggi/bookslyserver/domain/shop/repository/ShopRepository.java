package kyonggi.bookslyserver.domain.shop.repository;

import jakarta.persistence.EntityManager;
import kyonggi.bookslyserver.domain.shop.entity.Shop.Shop;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShopRepository extends JpaRepository<Shop,Long> {
    boolean existsByName(String name);
}
