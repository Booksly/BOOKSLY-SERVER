package kyonggi.bookslyserver.domain.reservation.repository.CustomRepository;

import com.querydsl.core.types.Expression;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import kyonggi.bookslyserver.domain.reservation.dto.ReserveResponseDTO;
import kyonggi.bookslyserver.domain.reservation.entity.QReservation;
import kyonggi.bookslyserver.domain.reservation.entity.QReservationSchedule;
import kyonggi.bookslyserver.domain.shop.entity.Employee.QEmployee;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class ReservationRepositoryCustomImpl implements ReservationRepositoryCustom{
    private final JPAQueryFactory queryFactory;
    QReservation reservation=QReservation.reservation;
    QReservationSchedule reservationSchedule=QReservationSchedule.reservationSchedule;
    QEmployee employee=QEmployee.employee;
    @Override
    public List<ReserveResponseDTO.getReservationRequestResultDTO> getReservationRequest(Long shopId) {
        LocalDateTime now = LocalDateTime.now();
        JPAQuery<ReserveResponseDTO.getReservationRequestResultDTO> query = createReservationRequestBaseQuery(now);
        query.where(reservationSchedule.shop.id.eq(shopId));
        return query.orderBy(reservation.createDate.desc()).fetch();
    }

    @Override
    public List<ReserveResponseDTO.getReservationRequestResultDTO> getImminentReservationRequest(Long shopId) {
        LocalDateTime now = LocalDateTime.now();
        Expression<LocalDateTime> reservationDateTime = Expressions.dateTimeTemplate(LocalDateTime.class,
                "CAST(CONCAT({0}, 'T', {1}) AS TIMESTAMP)", reservationSchedule.workDate, reservationSchedule.startTime);
        OrderSpecifier<?> orderByNearest = Expressions.numberTemplate(Long.class,
                "TIMESTAMPDIFF(SECOND, NOW(), {0})", reservationDateTime).asc();

        JPAQuery<ReserveResponseDTO.getReservationRequestResultDTO> query = createReservationRequestBaseQuery(now);
        return query.orderBy(orderByNearest).fetch();
    }

    private JPAQuery<ReserveResponseDTO.getReservationRequestResultDTO> createReservationRequestBaseQuery(LocalDateTime now) {
        return queryFactory
                .select(Projections.constructor(ReserveResponseDTO.getReservationRequestResultDTO.class,
                        reservation.id.as("reservationId"),
                        reservationSchedule.workDate.as("reservationDate"),
                        reservationSchedule.startTime.as("reservationTime"),
                        employee.name.as("employeeName")))
                .from(reservation)
                .join(reservation.reservationSchedule, reservationSchedule)
                .join(reservationSchedule.employee, employee)
                .where(reservation.isConfirmed.eq(false)
                        .and(reservation.isRefused.eq(false))
                        .and(isValidReservationSchedule(now)));
    }

    @Override
    public List<ReserveResponseDTO.getDatesWithResReqResultDTO> getDatesWithResReqResultDTO(int year, int month, Long shopId) {
        YearMonth yearMonth=YearMonth.of(year,month);

        LocalDate startDate=yearMonth.atDay(1);
        LocalDate endDate=yearMonth.atEndOfMonth();

        return queryFactory
                .select(Projections.constructor(ReserveResponseDTO.getDatesWithResReqResultDTO.class,
                reservation.reservationSchedule.workDate.as("reservationDate")))
                .from(reservation)
                .innerJoin(reservation.reservationSchedule,reservationSchedule)
                .where(isReservationRequestInTheMonth(startDate,endDate)
                        .and(reservation.reservationSchedule.shop.id.eq(shopId))
                        .and(reservation.isRefused.eq(false).and(reservation.isConfirmed.eq(false)))
                )
                .groupBy(reservation.reservationSchedule.workDate)
                .fetch();

    }

    private BooleanExpression isValidReservationSchedule(LocalDateTime now){
        return reservationSchedule.workDate.after(now.toLocalDate())
                .or(reservationSchedule.workDate.eq(now.toLocalDate()).and(reservationSchedule.startTime.after(now.toLocalTime())));
    }

    private BooleanExpression isReservationRequestInTheMonth(LocalDate startDate,LocalDate endDate){
        return reservationSchedule.workDate.between(startDate, endDate);
    }
}