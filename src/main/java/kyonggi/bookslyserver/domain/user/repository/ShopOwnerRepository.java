package kyonggi.bookslyserver.domain.user.repository;

import kyonggi.bookslyserver.domain.user.entity.ShopOwner;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShopOwnerRepository extends JpaRepository<ShopOwner, Long> {

}
