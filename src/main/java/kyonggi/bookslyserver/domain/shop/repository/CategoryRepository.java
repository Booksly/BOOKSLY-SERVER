package kyonggi.bookslyserver.domain.shop.repository;

import kyonggi.bookslyserver.domain.shop.constant.CategoryName;
import kyonggi.bookslyserver.domain.shop.entity.Shop.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    @Query("select c.id from Category c where c.id in :categoryIds")
    List<Long> findByCategoryIds(@RequestParam("categoryIds") List<Long> categoryIds);

    @Query("select c from Category c where c.categoryName = :categoryName")
    Category findByCategoryName(@Param("categoryName") CategoryName categoryName);
}
