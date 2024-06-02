package kyonggi.bookslyserver.domain.shop.repository;


import jakarta.transaction.Transactional;
import kyonggi.bookslyserver.domain.shop.entity.Menu.Menu;
import kyonggi.bookslyserver.domain.shop.entity.Shop.Shop;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MenuRepository extends JpaRepository<Menu, Long> {

    @Modifying
    @Transactional
    @Query("delete from Menu m where m.id = :menu_id")
    void delete(@Param("menu_id") Long id);

    @Query("select count(m) > 0 from Menu m where m.menuName = :name and m.menuCategory.id = :menuCategoryId")
    boolean existsNameInCategory(@Param("name") String menuName, @Param("menuCategoryId") Long menuCategoryId);


    @Query("select m from Menu m where m.shop = :shop")
    List<Menu> findMenusByShop(@Param("shop") Shop shop);

    @Query("SELECT m FROM Menu m JOIN FETCH m.menuCategory WHERE m.shop = :shop")
    List<Menu> findMenusByShopWithCategories(@Param("shop") Shop shop);

    @Query("select m from Menu m join fetch m.menuImage where m.id in :menuIds")
    List<Menu> findMenusByIdsWithImage(@Param("menuIds") List<Long> menuIds);
}
