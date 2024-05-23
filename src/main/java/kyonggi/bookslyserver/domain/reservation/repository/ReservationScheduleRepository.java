package kyonggi.bookslyserver.domain.reservation.repository;

import kyonggi.bookslyserver.domain.event.entity.closeEvent.ClosingEvent;
import kyonggi.bookslyserver.domain.event.entity.timeEvent.TimeEventSchedule;
import kyonggi.bookslyserver.domain.reservation.entity.ReservationSchedule;
import kyonggi.bookslyserver.domain.shop.entity.Employee.Employee;
import kyonggi.bookslyserver.domain.shop.entity.Shop.Shop;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collection;
import java.util.List;

public interface ReservationScheduleRepository extends JpaRepository<ReservationSchedule,Long> {
    List<ReservationSchedule> findByEmployeeAndWorkDate(Employee employee, LocalDate workDate);

    @Query("SELECT rs FROM ReservationSchedule rs where rs.employee = :employee and rs.workDate > :date")
    List<ReservationSchedule> findReservationSchedulesByEmployeeAndDateAfter(@Param("employee") Employee employee, @Param("date") LocalDate date);


    @Query("select rs from ReservationSchedule rs " +
            "where rs.workDate >= :eventStartDate and rs.workDate <= :eventEndDate and rs.employee = :em")
    List<ReservationSchedule> findOverlappingReservationSchedulesWithinEventPeriod(@Param("eventStartDate") LocalDate eventStartDate, @Param("eventEndDate") LocalDate eventEndDate,
                                                                                   @Param("em")Employee employee);


    @Query("select rs.timeEventSchedule from ReservationSchedule rs where rs.employee = :employee")
    List<TimeEventSchedule> findTimeEventSchedulesBy(@Param("employee")Employee employee);


    List<ReservationSchedule> findByClosingEvent(ClosingEvent closingEvent);

    @Query("select rs from ReservationSchedule rs where rs.timeEventSchedule IN :timeEventSchedules")
    List<ReservationSchedule> findByTimeEventSchedules(@Param(("timeEventSchedules")) List<TimeEventSchedule> timeEventSchedules);

    @Query("select rs from ReservationSchedule rs " +
            "where rs.workDate = :today and rs.startTime >= :startTime and rs.startTime <= :endTime " +
            "and rs.isClosingEvent = true and rs.isClosed = false and rs.shop = :shop " +
            "order by rs.startTime asc")
    List<ReservationSchedule> findWithAppliedClosingEvent(@Param("today") LocalDate nowDate,
                                           @Param("startTime") LocalTime startTime,
                                           @Param("endTime") LocalTime endTime,
                                           @Param("shop") Shop shop, Pageable pageable);


    @Query("select rs from ReservationSchedule rs " +
            "where rs.workDate = :today and rs.startTime >= :startTime and rs.startTime <= :endTime " +
            "and rs.timeEventSchedule.id is not null and rs.isClosed = false and rs.shop = :shop " +
            "order by rs.startTime asc")
    List<ReservationSchedule> findTimeEventSchedules(@Param("today") LocalDate nowDate,
                                                          @Param("startTime") LocalTime startTime,
                                                          @Param("endTime") LocalTime endTime,
                                                          @Param("shop") Shop shop, Pageable pageable);

}