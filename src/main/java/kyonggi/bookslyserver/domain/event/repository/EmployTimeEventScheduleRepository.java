package kyonggi.bookslyserver.domain.event.repository;

import kyonggi.bookslyserver.domain.event.entity.timeEvent.EmployTimeEventSchedule;
import kyonggi.bookslyserver.domain.shop.entity.Employee.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;

public interface EmployTimeEventScheduleRepository extends JpaRepository<EmployTimeEventSchedule, Long> {
    @Query("select COUNT(ts.id) > 0 from EmployTimeEventSchedule ts " +
            "WHERE ts.endEventDateTime > :startDateTime and ts.startEventDateTime < :endDateTime and ts.employee = :em")
    boolean existsOverlappingEventScheduleWithinEventPeriod(@Param("startDateTime") LocalDateTime newOpenEventDateTime,
                                                            @Param("endDateTime") LocalDateTime newEndEventDateTime,
                                                            @Param("em") Employee employee);
}
