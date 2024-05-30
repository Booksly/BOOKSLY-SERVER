package kyonggi.bookslyserver.domain.shop.repository;

import jakarta.transaction.Transactional;
import kyonggi.bookslyserver.domain.shop.entity.Menu.Menu;
import kyonggi.bookslyserver.domain.shop.entity.Menu.MenuCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface MenuCategoryRepository extends JpaRepository<MenuCategory, Long> {
    @Modifying
    @Transactional
    @Query("delete from MenuCategory mc where mc.id = :categoryId")
    void delete(@Param("categoryId") Long id);

    @Query("select count(mc) > 0 from MenuCategory mc where mc.name = :name and mc.shop.shopOwner.id = :ownerId")
    boolean existsByNameAndShopOwner(@Param("name") String name, @Param("ownerId") Long ownerId);

    @Query("select mc from MenuCategory mc where mc.name = :name and mc.shop.shopOwner.id = :ownerId")
    Optional<MenuCategory> findByNameAndShopOwner(@Param("name") String name, @Param("ownerId") Long ownerId);

    MenuCategory findByName(String name);
}
