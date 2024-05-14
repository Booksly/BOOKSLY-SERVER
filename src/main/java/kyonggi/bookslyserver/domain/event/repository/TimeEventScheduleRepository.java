package kyonggi.bookslyserver.domain.event.repository;

import kyonggi.bookslyserver.domain.event.entity.timeEvent.TimeEventSchedule;
import kyonggi.bookslyserver.domain.event.entity.timeEvent.TimeEvent;
import kyonggi.bookslyserver.domain.shop.entity.Employee.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface TimeEventScheduleRepository extends JpaRepository<TimeEventSchedule, Long> {
    @Query("select COUNT(ts.id) > 0 from TimeEventSchedule ts " +
            "WHERE ts.endEventDateTime > :startDateTime and ts.startEventDateTime < :endDateTime and ts.employee = :em")
    boolean existsOverlappingEventScheduleWithinEventPeriod(@Param("startDateTime") LocalDateTime newOpenEventDateTime,
                                                            @Param("endDateTime") LocalDateTime newEndEventDateTime,
                                                            @Param("em") Employee employee);

    @Query("select ts.timeEvent from TimeEventSchedule ts " +
            "where ts.employee.id = :em and ts.startEventDateTime < :dateTimeEnd and ts.endEventDateTime > :dateTimeStart")
    Optional<List<TimeEvent>> findTimeEventsByEmployeeIdAndDateTime(@Param("em") Long employeeId, @Param("dateTimeStart") LocalDateTime dateTimeStart,
                                                                    @Param("dateTimeEnd") LocalDateTime dateTimeEnd);

}
