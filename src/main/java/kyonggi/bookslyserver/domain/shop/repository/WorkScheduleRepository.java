package kyonggi.bookslyserver.domain.shop.repository;

import jakarta.transaction.Transactional;
import kyonggi.bookslyserver.domain.shop.entity.Employee.Employee;
import kyonggi.bookslyserver.domain.shop.entity.Employee.EmployeeMenu;
import kyonggi.bookslyserver.domain.shop.entity.Employee.WorkSchedule;
import org.hibernate.jdbc.Work;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.DayOfWeek;
import java.util.List;

@Repository
public interface WorkScheduleRepository extends JpaRepository<WorkSchedule, Long> {

    @Query("select ws from WorkSchedule ws where ws.employee.id = :employee_id")
    List<WorkSchedule> findByEmployeeId(@Param("employee_id") Long id);

}
