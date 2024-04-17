package kyonggi.bookslyserver.domain.reservation.repository;

import kyonggi.bookslyserver.domain.reservation.entity.ReservationSchedule;
import kyonggi.bookslyserver.domain.shop.entity.Employee.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface ReservationScheduleRepository extends JpaRepository<ReservationSchedule,Long> {
    List<ReservationSchedule> findByEmployeeAndWorkDate(Employee employee, LocalDate workDate);
}