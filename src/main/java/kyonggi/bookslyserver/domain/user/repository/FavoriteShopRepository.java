package kyonggi.bookslyserver.domain.user.repository;

import kyonggi.bookslyserver.domain.user.entity.FavoriteShop;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FavoriteShopRepository extends JpaRepository<FavoriteShop, Long> {
}
