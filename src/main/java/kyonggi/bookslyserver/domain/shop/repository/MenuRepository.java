package kyonggi.bookslyserver.domain.shop.repository;

import kyonggi.bookslyserver.domain.shop.entity.Menu.Menu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MenuRepository extends JpaRepository<Menu, Long> {
}
