package kyonggi.bookslyserver.domain.shop.repository;

import jakarta.transaction.Transactional;
import kyonggi.bookslyserver.domain.shop.dto.response.employee.EventRegisterEmployeeNamesDto;
import kyonggi.bookslyserver.domain.shop.entity.Employee.Employee;
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
    List<Employee> findEmployeeNames(@Param("shopId") Long id);
}