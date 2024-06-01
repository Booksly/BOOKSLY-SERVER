package kyonggi.bookslyserver.domain.shop.repository;

import jakarta.transaction.Transactional;
import kyonggi.bookslyserver.domain.shop.entity.Employee.Employee;
import kyonggi.bookslyserver.domain.shop.entity.Shop.Shop;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employee,Long> {

    Optional<Employee> findByIdAndShopId(Long employeeId, Long shopId);

    @Modifying
    @Transactional
    @Query("select e from Employee e where e.shop.id = :shopId")
    List<Employee> findEmployeesByShopId(@Param("shopId") Long id);

    @Query("SELECT CASE WHEN COUNT(e) > 0 THEN true ELSE false END " +
            "FROM Employee e WHERE e.id = :employeeId AND e.shop.id = :shopId")
    boolean existsByIdAndShopId(@Param("employeeId") Long employeeId, @Param("shopId") Long shopId);

    @Query("select count(e) from Employee e where e in :employees and e.shop.id = :shop")
    int countByEmployeesAndShop(@Param("employees") List<Employee> employees, @Param("shop") Long shopId);

    @Query("select e from Employee e join fetch e.employeeMenus em join fetch em.menu where e.id in :employeeIds")
    List<Employee> findAllById(@Param("employeeIds") List<Long> employeeIds);

    boolean existsByName(String name);

    Optional<List<Employee>> findByShop(Shop shop);
}