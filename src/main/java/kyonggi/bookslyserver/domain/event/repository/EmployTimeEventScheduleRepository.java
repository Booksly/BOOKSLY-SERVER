package kyonggi.bookslyserver.domain.event.repository;

import kyonggi.bookslyserver.domain.event.entity.timeEvent.EmployTimeEventSchedule;
import kyonggi.bookslyserver.domain.event.entity.timeEvent.TimeEvent;
import kyonggi.bookslyserver.domain.shop.entity.Employee.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface EmployTimeEventScheduleRepository extends JpaRepository<EmployTimeEventSchedule, Long> {
    @Query("select COUNT(ts.id) > 0 from EmployTimeEventSchedule ts " +
            "WHERE ts.endEventDateTime > :startDateTime and ts.startEventDateTime < :endDateTime and ts.employee = :em")
    boolean existsOverlappingEventScheduleWithinEventPeriod(@Param("startDateTime") LocalDateTime newOpenEventDateTime,
                                                            @Param("endDateTime") LocalDateTime newEndEventDateTime,
                                                            @Param("em") Employee employee);

    @Query("select et.timeEvent from EmployTimeEventSchedule et " +
            "where et.employee.id = :em and et.startEventDateTime < :dateTimeEnd and et.endEventDateTime > :dateTimeStart")
    Optional<List<TimeEvent>> findTimeEventsByEmployeeIdAndDateTime(@Param("em") Long employeeId, @Param("dateTimeStart") LocalDateTime dateTimeStart,
                                                                    @Param("dateTimeEnd") LocalDateTime dateTimeEnd);

}
