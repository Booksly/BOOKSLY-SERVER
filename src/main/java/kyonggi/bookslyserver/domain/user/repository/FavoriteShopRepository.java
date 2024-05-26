package kyonggi.bookslyserver.domain.user.repository;

import kyonggi.bookslyserver.domain.user.entity.FavoriteShop;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FavoriteShopRepository extends JpaRepository<FavoriteShop, Long> {
    @Query("select f from FavoriteShop f join fetch f.shop s join fetch s.category join fetch s.address where f.user.id = :userId")
    List<FavoriteShop> findByUserId(@Param("userId") Long userId);
}
