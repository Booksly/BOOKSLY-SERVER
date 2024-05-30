package kyonggi.bookslyserver.domain.shop.repository;

import jakarta.transaction.Transactional;
import kyonggi.bookslyserver.domain.shop.entity.Employee.Employee;
import kyonggi.bookslyserver.domain.shop.entity.Employee.EmployeeMenu;
import kyonggi.bookslyserver.domain.shop.entity.Menu.Menu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeMenuRepository extends JpaRepository<EmployeeMenu,Long> {

    @Modifying
    @Transactional
    @Query("select em from EmployeeMenu em where em.employee.id = :employee_id")
    List<EmployeeMenu> findByEmployeeId(@Param("employee_id") Long id);

    @Query("SELECT COUNT(em) "+
            "FROM EmployeeMenu em WHERE em.employee = :employee_id AND em.menu IN :menus")
    int countByMenusAndEmployee(@Param("menus") List<Menu> menus, @Param("employee_id") Employee employee);

    @Query("SELECT em.menu "+
            "FROM EmployeeMenu em WHERE em.employee = :employee_id AND em.menu IN :menus")
    List<Menu> findByMenusAndEmployee(@Param("menus") List<Menu> menus, @Param("employee_id") Employee employee);

    @Query("select em.menu from EmployeeMenu em where em.menu.id in :menuIds and em.employee in :employees")
    List<Menu> findMenusByMenuIdsAndEmployees(@Param("menuIds") List<Long> menus, @Param("employees")List<Employee> employees);

    Optional<EmployeeMenu> findByEmployeeAndMenu(Employee employee,Menu menu);
}
