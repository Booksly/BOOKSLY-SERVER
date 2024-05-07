package kyonggi.bookslyserver.domain.reservation.repository.CustomRepository;

import com.querydsl.core.Tuple;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.querydsl.jpa.sql.JPASQLQuery;
import kyonggi.bookslyserver.domain.reservation.dto.ReserveResponseDTO;
import kyonggi.bookslyserver.domain.reservation.entity.QReservation;
import kyonggi.bookslyserver.domain.reservation.entity.QReservationMenu;
import kyonggi.bookslyserver.domain.reservation.entity.QReservationSchedule;
import kyonggi.bookslyserver.domain.shop.entity.Employee.QEmployee;
import kyonggi.bookslyserver.domain.shop.entity.Menu.QMenu;
import lombok.RequiredArgsConstructor;
import org.hibernate.query.criteria.JpaSubQuery;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class ReservationRepositoryCustomImpl implements ReservationRepositoryCustom{
    private final JPAQueryFactory queryFactory;
    QReservation reservation=QReservation.reservation;
    QReservationSchedule reservationSchedule=QReservationSchedule.reservationSchedule;
    QEmployee employee=QEmployee.employee;
    QReservationMenu reservationMenu=QReservationMenu.reservationMenu;
    QMenu menu=QMenu.menu;
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

    @Override
    public List<ReserveResponseDTO.getTodayReservationsResultDTO> getTodayReservationSchedules(LocalDate today, Long employeeId) {
        
        List<ReserveResponseDTO.getTodayReservationsResultDTO> results = queryFactory
                .select(Projections.fields(ReserveResponseDTO.getTodayReservationsResultDTO.class,
                        reservationSchedule.id.as("reservationScheduleId"),
                        reservationSchedule.startTime.as("reservationScheduleTime"),
                        reservationSchedule.isClosed.as("isClosed")
                ))
                .from(reservationSchedule)
                .where(reservationSchedule.employee.id.eq(employeeId),
                        reservationSchedule.workDate.eq(today)
                )
                .fetch();

        // 각 예약에 대한 메뉴 이름 목록 조회 후 reservationMenu 리스트 세팅
        results.forEach(result -> {
            List<String> menuNames = queryFactory
                    .select(menu.menuName)
                    .from(reservationMenu)
                    .leftJoin(menu).on(reservationMenu.menu.id.eq(menu.id))
                    .where(
                            reservationMenu.reservation.reservationSchedule.id.eq(result.getReservationScheduleId()),
                            reservationMenu.reservation.isConfirmed.eq(true)
                    )
                    .fetch()
                    .stream()
                    .distinct()
                    .toList();

            List<ReserveResponseDTO.reservationMenu> menuList = menuNames.stream()
                    .map(name -> ReserveResponseDTO.reservationMenu.builder().menuName(name).build())
                    .collect(Collectors.toList());

            result.setReservationMenus(menuList);
        });

        return results;
    }
    @Override
    public List<ReserveResponseDTO.getTodayReservationsResultDTO> getTodayReservationsOnly(LocalDate date, Long employeeId) {

        List<ReserveResponseDTO.getTodayReservationsResultDTO> results = queryFactory
                .select(Projections.fields(ReserveResponseDTO.getTodayReservationsResultDTO.class,
                        reservationSchedule.id.as("reservationScheduleId"),
                        reservationSchedule.startTime.as("reservationScheduleTime"),
                        reservationSchedule.isClosed.as("isClosed")
                ))
                .from(reservationSchedule)
                .leftJoin(reservation)
                .on(reservation.reservationSchedule.id.eq(reservationSchedule.id))
                .where(reservationSchedule.employee.id.eq(employeeId),
                        reservationSchedule.workDate.eq(date),
                        reservation.id.isNotNull().and(reservation.isConfirmed.eq(true))
                )
                .groupBy(reservationSchedule.id)
                .fetch();

        results.forEach(result -> {
            List<String> menuNames = queryFactory
                    .select(menu.menuName)
                    .from(reservationMenu)
                    .leftJoin(menu).on(reservationMenu.menu.id.eq(menu.id))
                    .where(
                            reservationMenu.reservation.reservationSchedule.id.eq(result.getReservationScheduleId()),
                            reservationMenu.reservation.isRefused.eq(false)
                    )
                    .fetch()
                    .stream()
                    .distinct()
                    .toList();

            List<ReserveResponseDTO.reservationMenu> menuList = menuNames.stream()
                    .map(name -> ReserveResponseDTO.reservationMenu.builder().menuName(name).build())
                    .collect(Collectors.toList());

            result.setReservationMenus(menuList);
        });

        return results;
    }

    private BooleanExpression isValidReservationSchedule(LocalDateTime now){
        return reservationSchedule.workDate.after(now.toLocalDate())
                .or(reservationSchedule.workDate.eq(now.toLocalDate()).and(reservationSchedule.startTime.after(now.toLocalTime())));
    }

    private BooleanExpression isReservationRequestInTheMonth(LocalDate startDate,LocalDate endDate){
        return reservationSchedule.workDate.between(startDate, endDate);
    }
}