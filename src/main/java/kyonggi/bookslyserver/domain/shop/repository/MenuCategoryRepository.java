package kyonggi.bookslyserver.domain.shop.repository;

import jakarta.transaction.Transactional;
import kyonggi.bookslyserver.domain.shop.entity.Menu.MenuCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MenuCategoryRepository extends JpaRepository<MenuCategory, Long> {
    @Modifying
    @Transactional
    @Query("delete from MenuCategory mc where mc.id = :categoryId")
    void delete(@Param("categoryId") Long id);

    boolean existsByName(String name);

    MenuCategory findByName(String name);
}
