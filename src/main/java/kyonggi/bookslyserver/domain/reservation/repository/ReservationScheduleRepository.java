package kyonggi.bookslyserver.domain.reservation.repository;

import kyonggi.bookslyserver.domain.event.entity.timeEvent.EmployTimeEventSchedule;
import kyonggi.bookslyserver.domain.reservation.entity.ReservationSchedule;
import kyonggi.bookslyserver.domain.shop.entity.Employee.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface ReservationScheduleRepository extends JpaRepository<ReservationSchedule,Long> {
    List<ReservationSchedule> findByEmployeeAndWorkDate(Employee employee, LocalDate workDate);

    @Query("SELECT rs FROM ReservationSchedule rs where rs.employee = :employee and rs.workDate > :date")
    List<ReservationSchedule> findReservationSchedulesByEmployeeAndDateAfter(@Param("employee") Employee employee, @Param("date") LocalDate date);


    @Query("select rs from ReservationSchedule rs " +
            "where rs.workDate >= :eventStartDate and rs.workDate <= :eventEndDate and rs.employee = :em")
    List<ReservationSchedule> findOverlappingReservationSchedulesWithinEventPeriod(@Param("eventStartDate") LocalDate eventStartDate, @Param("eventEndDate") LocalDate eventEndDate,
                                                                                   @Param("em")Employee employee);


    @Query("select rs.employTimeEventSchedule from ReservationSchedule rs where rs.employee = :employee")
    List<EmployTimeEventSchedule> findTimeEventSchedulesBy(@Param("employee")Employee employee);


}