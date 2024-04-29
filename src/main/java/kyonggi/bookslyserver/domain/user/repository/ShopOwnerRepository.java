package kyonggi.bookslyserver.domain.user.repository;

import kyonggi.bookslyserver.domain.user.entity.ShopOwner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ShopOwnerRepository extends JpaRepository<ShopOwner, Long> {

    @Query("SELECT owner FROM ShopOwner owner where owner.user.id = :id")
    ShopOwner findByUserId(@Param("id") Long id);

    @Query("SELECT owner.user.loginId FROM ShopOwner owner where owner.id = :id")
    Optional<String> findLoginId(@Param("id") Long id);

}
