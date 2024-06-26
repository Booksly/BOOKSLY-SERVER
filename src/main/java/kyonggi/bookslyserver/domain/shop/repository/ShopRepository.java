package kyonggi.bookslyserver.domain.shop.repository;

import kyonggi.bookslyserver.domain.shop.entity.Shop.Category;
import kyonggi.bookslyserver.domain.shop.entity.Shop.Shop;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Repository;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collection;
import java.util.List;

@Repository
public interface ShopRepository extends JpaRepository<Shop,Long> {
    boolean existsByName(String name);

    @Query("select s from Shop s where s.shopOwner.id = :id")
    List<Shop> findByShopOwnerId(@Param("id") Long id);


    @Query("select s from Shop s where s.createDate <= :currentDate and s.createDate >= :flagDate")
    Page<Shop> findNewShops(Pageable pageable, @Param("currentDate") LocalDateTime currentDate, @Param("flagDate")LocalDateTime flagDate);

    @Query("select s from Shop s where s.address.firstAddress = :firstAddress and s.address.secondAddress = :secondAddress and s IN :shops")
    List<Shop> findShopsByOneAndTwoAddress(@Param("firstAddress") String firstAddress, @Param("secondAddress") String secondAddress, @Param("shops") List<Shop> shops);

    @Query("select s from Shop s where s.address.firstAddress = :firstAddress and s.address.secondAddress = :secondAddress and s.address.thirdAddress = :thirdAddress and s IN :shops")
    List<Shop> findShopsByAddress(@Param("firstAddress") String firstAddress, @Param("secondAddress") String secondAddress, @Param("thirdAddress") String thirdAddress, @Param("shops") List<Shop> shops);

    @Query("select s from Shop s where s.category.id in :categoryIds and s in :shops")
    List<Shop> findByCategoryIds(@Param("categoryIds") List<Long> categoryIds, @Param("shops") List<Shop> shops);

}
