package kyonggi.bookslyserver.domain.event.repository;

import kyonggi.bookslyserver.domain.event.entity.timeEvent.EmployeeTimeEvent;
import kyonggi.bookslyserver.domain.event.entity.timeEvent.TimeEvent;
import kyonggi.bookslyserver.domain.shop.entity.Employee.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeTimeEventRepository extends JpaRepository<EmployeeTimeEvent, Long> {

    @Query("SELECT ete.timeEvent FROM EmployeeTimeEvent ete WHERE ete.employee = :employee")
    TimeEvent findTimeEventByEmployee(@Param("employee") Employee employee);

    @Query("select ete.timeEvent from EmployeeTimeEvent ete where ete.employee = :employee")
    TimeEvent findByEmployee(@Param("employee") Employee employee);

    int countByTimeEvent(TimeEvent timeEvent);
}
