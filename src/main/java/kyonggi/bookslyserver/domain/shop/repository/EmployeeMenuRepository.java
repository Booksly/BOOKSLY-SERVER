package kyonggi.bookslyserver.domain.shop.repository;

import jakarta.transaction.Transactional;
import kyonggi.bookslyserver.domain.shop.entity.Employee.EmployeeMenu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployeeMenuRepository extends JpaRepository<EmployeeMenu,Long> {

    boolean existsByMenuId(Long menuId);

    @Modifying
    @Transactional
    @Query("select em from EmployeeMenu em where em.employee.id = :employee_id")
    List<EmployeeMenu> findByEmployeeId(@Param("employee_id") Long id);
}
