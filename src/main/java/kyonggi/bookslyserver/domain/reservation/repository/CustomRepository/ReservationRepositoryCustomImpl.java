package kyonggi.bookslyserver.domain.reservation.repository.CustomRepository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import kyonggi.bookslyserver.domain.reservation.dto.ReserveResponseDTO;
import kyonggi.bookslyserver.domain.reservation.entity.QReservation;
import kyonggi.bookslyserver.domain.reservation.entity.QReservationSchedule;
import kyonggi.bookslyserver.domain.shop.entity.Employee.QEmployee;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ReservationRepositoryCustomImpl implements ReservationRepositoryCustom{
    private final JPAQueryFactory queryFactory;
    QReservation reservation=QReservation.reservation;
    QReservationSchedule reservationSchedule=QReservationSchedule.reservationSchedule;
    QEmployee employee=QEmployee.employee;
    @Override
    public List<ReserveResponseDTO.getReservationRequestResultDTO> getReservationRequest() {
        return queryFactory
                .select(Projections.constructor(ReserveResponseDTO.getReservationRequestResultDTO.class,
                        reservation.id.as("reservationId"),
                        reservationSchedule.workDate.as("reservationDate"),
                        reservationSchedule.startTime.as("reservationTime"),
                        employee.name.as("employeeName")))
                .from(reservation)
                .join(reservation.reservationSchedule,reservationSchedule)
                .join(reservationSchedule.employee,employee)
                .where(reservation.isConfirmed.eq(false).and(reservation.isRefused.eq(false)))
                .fetch();
    }
}
