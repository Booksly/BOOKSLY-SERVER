package kyonggi.bookslyserver.domain.shop.repository;

import kyonggi.bookslyserver.domain.shop.entity.Menu.MenuImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MenuImageRepository extends JpaRepository<MenuImage, Long> {
    @Modifying
    @Query("delete from MenuImage mi where mi.menuImgUri = :uri")
    void delete(@Param("uri") String image);
}
