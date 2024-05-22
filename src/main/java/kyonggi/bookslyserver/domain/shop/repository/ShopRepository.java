package kyonggi.bookslyserver.domain.shop.repository;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import kyonggi.bookslyserver.domain.shop.entity.Shop.Shop;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShopRepository extends JpaRepository<Shop,Long> {
    boolean existsByName(String name);

    @Query("select s from Shop s where s.shopOwner.id = :id")
    List<Shop> findByShopOwnerId(@Param("id") Long id);

}
