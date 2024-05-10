package kyonggi.bookslyserver.domain.reservation.repository.CustomRepository;

import com.querydsl.core.Tuple;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import kyonggi.bookslyserver.domain.reservation.dto.ReserveResponseDTO;
import kyonggi.bookslyserver.domain.reservation.entity.QReservation;
import kyonggi.bookslyserver.domain.reservation.entity.QReservationMenu;
import kyonggi.bookslyserver.domain.reservation.entity.QReservationSchedule;
import kyonggi.bookslyserver.domain.shop.entity.Employee.QEmployee;
import kyonggi.bookslyserver.domain.shop.entity.Menu.QMenu;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.YearMonth;
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
    public List<ReserveResponseDTO.getTodayReservationSchedulesResultDTO> getTodayReservationSchedules(LocalDate today, Long employeeId) {
        
        List<ReserveResponseDTO.getTodayReservationSchedulesResultDTO> results = queryFactory
                .select(Projections.fields(ReserveResponseDTO.getTodayReservationSchedulesResultDTO.class,
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
    public List<ReserveResponseDTO.getOnlyReservationsOfDateResultDTO> getOnlyReservationsOfDate(LocalDate date, Long employeeId) {

        List<ReserveResponseDTO.getOnlyReservationsOfDateResultDTO> results = queryFactory
                .select(Projections.fields(ReserveResponseDTO.getOnlyReservationsOfDateResultDTO.class,
                        reservationSchedule.id.as("reservationScheduleId"),
                        reservationSchedule.startTime.as("reservationScheduleTime")
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

    @Override
    public List<ReserveResponseDTO.getTodayReservationsAllEmpsResultDTO> getTodayReservationsScheduleAllEmps(LocalDate today, Long shopId) {
        List<Tuple> results=queryFactory
                .select(reservationSchedule.startTime,menu.menuName)
                .from(reservationSchedule)
                .join(reservationSchedule.reservations, reservation)
                .join(reservation.reservationMenus, reservationMenu)
                .join(reservationMenu.menu, menu)
                .where(reservationSchedule.workDate.eq(today)
                        .and(reservationSchedule.shop.id.eq(shopId))
                        .and(reservation.isConfirmed.eq(true))
                )
                .fetch();

        // startTime 기준으로 그룹화
        Map<LocalTime,List<String>> groupByStartTime=results.stream()
                .collect(Collectors.groupingBy(
                   tuple->tuple.get(reservationSchedule.startTime),
                   Collectors.mapping(tuple->tuple.get(menu.menuName),Collectors.toList())
                ));
        // DTO로 변환
        return groupByStartTime.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .map(entry->new ReserveResponseDTO.getTodayReservationsAllEmpsResultDTO(entry.getKey(),entry.getValue().stream()
                        .distinct()
                        .map(menuName->new ReserveResponseDTO.reservationMenu(menuName))
                        .collect(Collectors.toList())))
                .collect(Collectors.toList());
    }

    @Override
    public List<ReserveResponseDTO.getTodayReservationsDetailsResultDTO> getTodayReservationsDetails(LocalDate today, Long employeeId) {
        List<ReserveResponseDTO.getTodayReservationsDetailsResultDTO> results=queryFactory.select(
                Projections.constructor(ReserveResponseDTO.getTodayReservationsDetailsResultDTO.class,
                        reservation.id.as("reservationId"),
                        reservation.reservationSchedule.startTime.as("reservationScheduleTime"),
                        reservation.inquiry.as("inquiry")
                        )
        )
                .from(reservation)
                .where(reservation.reservationSchedule.workDate.eq(today),
                        reservation.isConfirmed.eq(true),
                        reservation.reservationSchedule.employee.id.eq(employeeId))
                .orderBy(reservationSchedule.startTime.asc())
                .fetch();
        results.forEach(result->{
            List<String> menuNames=queryFactory
                    .select(menu.menuName)
                    .from(reservationMenu)
                    .leftJoin(menu).on(reservationMenu.menu.id.eq(menu.id))
                    .where(reservationMenu.reservation.id.eq(result.getReservationId()))
                    .fetch();
            result.setReservationMenus(menuNames);
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