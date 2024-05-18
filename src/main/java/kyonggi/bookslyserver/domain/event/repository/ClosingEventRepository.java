package kyonggi.bookslyserver.domain.event.repository;

import kyonggi.bookslyserver.domain.event.entity.closeEvent.ClosingEvent;
import kyonggi.bookslyserver.domain.shop.entity.Employee.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface ClosingEventRepository extends JpaRepository<ClosingEvent, Long> {


    @Query("SELECT ce FROM ClosingEvent ce WHERE ce.employee.id IN :employeeIds")
    Optional<List<ClosingEvent>> findByEmployeeIds(@Param("employeeIds") List<Long> employeeIds);

    Optional<ClosingEvent> findByEmployee(Employee employee);

    boolean existsByEmployeeId(Long employeeId);
}
