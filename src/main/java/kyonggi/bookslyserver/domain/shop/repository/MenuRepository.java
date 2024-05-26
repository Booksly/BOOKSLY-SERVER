package kyonggi.bookslyserver.domain.shop.repository;


import jakarta.transaction.Transactional;
import kyonggi.bookslyserver.domain.shop.entity.Menu.Menu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MenuRepository extends JpaRepository<Menu, Long> {
    Menu findByMenuName(String menuName);

    @Modifying
    @Transactional
    @Query("delete from Menu m where m.id = :menu_id")
    void delete(@Param("menu_id") Long id);
}
