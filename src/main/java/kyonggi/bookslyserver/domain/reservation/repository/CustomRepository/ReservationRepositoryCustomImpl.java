package kyonggi.bookslyserver.domain.reservation.repository.CustomRepository;

import com.querydsl.core.Tuple;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import kyonggi.bookslyserver.domain.event.entity.closeEvent.QClosingEvent;
import kyonggi.bookslyserver.domain.event.entity.timeEvent.QTimeEvent;
import kyonggi.bookslyserver.domain.event.entity.timeEvent.QTimeEventSchedule;
import kyonggi.bookslyserver.domain.reservation.dto.ReserveResponseDTO;
import kyonggi.bookslyserver.domain.reservation.entity.QReservation;
import kyonggi.bookslyserver.domain.reservation.entity.QReservationMenu;
import kyonggi.bookslyserver.domain.reservation.entity.QReservationSchedule;
import kyonggi.bookslyserver.domain.reservation.service.ReserveCommandService;
import kyonggi.bookslyserver.domain.review.entity.QReview;
import kyonggi.bookslyserver.domain.shop.entity.Employee.QEmployee;
import kyonggi.bookslyserver.domain.shop.entity.Menu.QMenu;
import kyonggi.bookslyserver.domain.shop.entity.Menu.QMenuCategory;
import kyonggi.bookslyserver.domain.shop.entity.Shop.QShop;
import kyonggi.bookslyserver.domain.user.entity.QFavoriteShop;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.YearMonth;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
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
    QShop shop=QShop.shop;
    QTimeEventSchedule timeEventSchedule=QTimeEventSchedule.timeEventSchedule;
    QTimeEvent timeEvent=QTimeEvent.timeEvent;
    QClosingEvent closingEvent=QClosingEvent.closingEvent;
    QMenuCategory menuCategory=QMenuCategory.menuCategory;
    QFavoriteShop favoriteShop=QFavoriteShop.favoriteShop;
    QReview review=QReview.review;
    /**
     * 예약 요청 조회
     */
    @Override
    public List<ReserveResponseDTO.getReservationRequestResultDTO> getReservationRequest(Long shopId) {
        LocalDateTime now = LocalDateTime.now();
        JPAQuery<ReserveResponseDTO.getReservationRequestResultDTO> query = createReservationRequestBaseQuery(now,shopId);
        return query.orderBy(reservation.createDate.desc()).fetch();
    }

    @Override
    public List<ReserveResponseDTO.getReservationRequestResultDTO> getImminentReservationRequest(Long shopId) {
        LocalDateTime now = LocalDateTime.now();
        Expression<LocalDateTime> reservationDateTime = Expressions.dateTimeTemplate(LocalDateTime.class,
                "CAST(CONCAT({0}, 'T', {1}) AS TIMESTAMP)", reservationSchedule.workDate, reservationSchedule.startTime);
        OrderSpecifier<?> orderByNearest = Expressions.numberTemplate(Long.class,
                "TIMESTAMPDIFF(SECOND, NOW(), {0})", reservationDateTime).asc();

        JPAQuery<ReserveResponseDTO.getReservationRequestResultDTO> query = createReservationRequestBaseQuery(now,shopId);
        return query.orderBy(orderByNearest).fetch();
    }
    /**
     * 예약 요청 조회 base query
     */
    private JPAQuery<ReserveResponseDTO.getReservationRequestResultDTO> createReservationRequestBaseQuery(LocalDateTime now,Long shopId) {
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
                        .and(isValidReservationSchedule(now)),
                        reservation.isCanceled.eq(false),
                        reservationSchedule.shop.id.eq(shopId)
                );
    }

    /**
     * 예약 요청 상세 조회
     */
    @Override
    public List<ReserveResponseDTO.getReservationRequestDetailsResultDTO> getReservationRequestDetails(Long shopId) {
        LocalDateTime now=LocalDateTime.now();
        List<ReserveResponseDTO.getReservationRequestDetailsResultDTO> results= createReservationReqDetailsBaseQuery(now,shopId).orderBy(reservation.createDate.desc()).fetch();
        setMenuNames(results);
        return results;
    }

    @Override
    public List<ReserveResponseDTO.getReservationRequestDetailsResultDTO> getImminentReservationRequestDetails(Long shopId) {
        LocalDateTime now = LocalDateTime.now();
        Expression<LocalDateTime> reservationDateTime = Expressions.dateTimeTemplate(LocalDateTime.class,
                "CAST(CONCAT({0}, 'T', {1}) AS TIMESTAMP)", reservationSchedule.workDate, reservationSchedule.startTime);
        OrderSpecifier<?> orderByNearest = Expressions.numberTemplate(Long.class,
                "TIMESTAMPDIFF(SECOND, NOW(), {0})", reservationDateTime).asc();
        List<ReserveResponseDTO.getReservationRequestDetailsResultDTO> results= createReservationReqDetailsBaseQuery(now,shopId).orderBy(orderByNearest).fetch();
        setMenuNames(results);
        return results;
    }
    private OrderSpecifier<?> orderByNearest(){
        Expression<LocalDateTime> reservationDateTime = Expressions.dateTimeTemplate(LocalDateTime.class,
                "CAST(CONCAT({0}, 'T', {1}) AS TIMESTAMP)", reservationSchedule.workDate, reservationSchedule.startTime);
        return Expressions.numberTemplate(Long.class,
                "TIMESTAMPDIFF(SECOND, NOW(), {0})", reservationDateTime).asc();
    }

    /**
     * 예약 요청 상세 조회 Base query
     */
    private JPAQuery<ReserveResponseDTO.getReservationRequestDetailsResultDTO> createReservationReqDetailsBaseQuery(LocalDateTime now,Long shopId){
        return queryFactory.
                select(Projections.fields(ReserveResponseDTO.getReservationRequestDetailsResultDTO.class,
                        reservation.id.as("reservationId"),
                        reservationSchedule.workDate.as("reservationDate"),
                        reservationSchedule.startTime.as("reservationTime"),
                        employee.name.as("employeeName"),
                        reservation.inquiry.as("inquiry")
                        )
                ).from(reservation)
                .join(reservation.reservationSchedule,reservationSchedule)
                .join(reservationSchedule.employee,employee)
                .where(reservation.isConfirmed.eq(false)
                                .and(reservation.isRefused.eq(false))
                                .and(isValidReservationSchedule(now)),
                        reservation.isCanceled.eq(false),
                        reservationSchedule.shop.id.eq(shopId));
    }
    private void setMenuNames(List<ReserveResponseDTO.getReservationRequestDetailsResultDTO> results){
        results.forEach(result ->{
            List<String> menuNames=queryFactory
                    .select(menu.menuName)
                    .from(reservationMenu)
                    .leftJoin(menu).on(reservationMenu.menu.id.eq(menu.id))
                    .where(reservationMenu.reservation.id.eq(result.getReservationId()))
                    .fetch();
            result.setMenuNames(menuNames);
        });
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
                        .and(reservation.isRefused.eq(false).and(reservation.isConfirmed.eq(false))),
                        reservation.isCanceled.eq(false)
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
                            reservationMenu.reservation.isConfirmed.eq(true),
                            reservationMenu.reservation.isCanceled.eq(false)
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
                            reservationMenu.reservation.isRefused.eq(false),
                            reservationMenu.reservation.isCanceled.eq(false)
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
                .leftJoin(reservationSchedule.reservations, reservation).on(reservation.isConfirmed.isTrue().and(reservation.isCanceled.isFalse()))
                .leftJoin(reservation.reservationMenus, reservationMenu)
                .leftJoin(reservationMenu.menu, menu)
                .where(reservationSchedule.workDate.eq(today)
                        .and(reservationSchedule.shop.id.eq(shopId))
                )
                .fetch();

        // startTime 기준으로 그룹화
        Map<LocalTime,List<String>> groupByStartTime=results.stream()
                .collect(Collectors.groupingBy(
                   tuple->tuple.get(reservationSchedule.startTime),
                   Collectors.mapping(tuple-> Optional.ofNullable(tuple.get(menu.menuName)).orElse(""),Collectors.toList())
                ));
        // DTO로 변환
        return groupByStartTime.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .map(entry->new ReserveResponseDTO.getTodayReservationsAllEmpsResultDTO(entry.getKey(),entry.getValue().stream()
                        .distinct()
                        .filter(menuName ->!menuName.isEmpty())
                        .map(menuName->new ReserveResponseDTO.reservationMenu(menuName))
                        .collect(Collectors.toList())))
                .collect(Collectors.toList());
    }

    @Override
    public List<ReserveResponseDTO.getTodayReservationsDetailsResultDTO> getTodayReservationsDetails(LocalDate today, Long employeeId) {
        List<ReserveResponseDTO.getTodayReservationsDetailsResultDTO> results=queryFactory.select(
                Projections.fields(ReserveResponseDTO.getTodayReservationsDetailsResultDTO.class,
                        reservation.id.as("reservationId"),
                        reservation.reservationSchedule.startTime.as("reservationScheduleTime"),
                        reservation.inquiry.as("inquiry")
                        )
        )
                .from(reservation)
                .where(reservation.reservationSchedule.workDate.eq(today),
                        reservation.isConfirmed.eq(true),
                        reservation.isCanceled.eq(false),
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

    @Override
    public List<ReserveResponseDTO.findTodayReservationsResultDTO> findTodayReservations(LocalDate date, List<ReserveCommandService.AddressRange> addressRanges,List<ReserveCommandService.TimeRange> timeRanges, List<Long> categories) {

        List<ReserveResponseDTO.findTodayReservationsResultDTO> results=queryFactory.
                select(Projections.fields(ReserveResponseDTO.findTodayReservationsResultDTO.class,
                        reservationSchedule.id.as("reservationScheduleId"),
                        reservationSchedule.workDate.as("date"),
                        reservationSchedule.startTime.as("time"),
                        shop.name.as("shopName"),
                        Expressions.stringTemplate("CONCAT({0},' ',{1},' ',{2})",
                                shop.address.firstAddress,
                                shop.address.secondAddress,
                                shop.address.thirdAddress
                                ).as("location")
                        ))
                .from(reservationSchedule)
                .join(reservationSchedule.shop,shop)
                .where(reservationSchedule.workDate.eq(date),
                        reservationSchedule.isClosed.eq(false),
                        isReservationSchInTimeRange(timeRanges),
                        isShopInAddressRange(addressRanges),
                        shop.category.id.in(categories)
                        )
                .orderBy(orderByNearest())
                .fetch();
        results.forEach(
                result->{
                    int totalDcRate=0;
                    ReserveResponseDTO.timeEventInfo info=queryFactory
                            .select(Projections.fields(ReserveResponseDTO.timeEventInfo.class,
                                    timeEvent.title.as("timeEventTitle"),
                                    timeEvent.discountRate.as("timeDc")
                                    ))
                            .from(reservationSchedule)
                            .join(reservationSchedule.timeEventSchedule,timeEventSchedule)
                            .join(timeEventSchedule.timeEvent,timeEvent)
                            .where(reservationSchedule.id.eq(result.getReservationScheduleId()))
                            .fetchOne();
                    if (info!=null) totalDcRate+=info.getTimeDc();
                    result.setTimeEvent(info);
                    ReserveResponseDTO.closeEventInfo info2=queryFactory
                            .select(Projections.fields(ReserveResponseDTO.closeEventInfo.class,
                                    closingEvent.eventMessage.as("closeEventTitle"),
                                    closingEvent.discountRate.as("closeDc")
                            ))
                            .from(reservationSchedule)
                            .join(reservationSchedule.closingEvent,closingEvent)
                            .where(reservationSchedule.id.eq(result.getReservationScheduleId()))
                            .fetchOne();
                    if (info2!=null) totalDcRate+=info2.getCloseDc();
                    result.setCloseEvent(info2);
                    result.setTotalDcRate(totalDcRate);
                }
        );
        return results;
    }
    @Override
    public List<ReserveResponseDTO.findTodayReservationsResultDTO> findTodayReservationsByDiscount(LocalDate date, List<ReserveCommandService.AddressRange> addressRanges, List<ReserveCommandService.TimeRange> timeRanges, List<Long> categories) {
        List<ReserveResponseDTO.findTodayReservationsResultDTO> results= findTodayReservations(date,addressRanges, timeRanges, categories);
        return results.stream()
                .sorted(Comparator.comparing(ReserveResponseDTO.findTodayReservationsResultDTO::getTotalDcRate).reversed())
                .collect(Collectors.toList());
    }

    @Override
    public List<ReserveResponseDTO.myPageReservationsResultDTO> getAllReservationRecords(Long userId, Long categoryId, boolean now) {
        List<ReserveResponseDTO.myPageReservationsResultDTO> results=queryFactory.select(
                Projections.fields(ReserveResponseDTO.myPageReservationsResultDTO.class,
                        reservation.id.as("reservationId"),
                        reservation.timeEventTitle.as("eventTitle"),
                        reservation.isRefused.as("isRefused"),
                        reservation.isCanceled.as("isCanceled"),
                        reservationSchedule.workDate.as("date"),
                        reservationSchedule.startTime.as("time"),
                        shop.name.as("shopName"),
                        Expressions.stringTemplate("CAST({0} AS string)",shop.category.categoryName).as("shopCategory"),
                        Expressions.stringTemplate("CONCAT({0},' ',{1},' ',{2})",
                                shop.address.firstAddress,
                                shop.address.secondAddress,
                                shop.address.thirdAddress
                        ).as("location"),
                        ExpressionUtils.as(
                                JPAExpressions.selectOne()
                                        .from(favoriteShop)
                                        .where(favoriteShop.user.id.eq(userId)
                                                .and(favoriteShop.shop.id.eq(shop.id)))
                                        .exists(),"isFavor"
                        ),
                        employee.name.as("employeeName"),
                        ExpressionUtils.as(JPAExpressions.selectOne()
                                .from(review)
                                .where(review.reservation.id.eq(reservation.id))
                                .exists(),"hasReview")
                ))
                .from(reservation)
                .join(reservation.reservationSchedule,reservationSchedule)
                .join(reservationSchedule.shop,shop)
                .join(reservationSchedule.employee,employee)
                .where(reservation.user.id.eq(userId), hasToClassifyCategory(categoryId),forNowReservations(now),reservation.isDeleted.isFalse())
                .fetch();
        results.forEach(
                result -> {
                    List<Tuple> menus= queryFactory.select(
                            menuCategory.name, menu.menuName
                            )
                            .from(reservationMenu)
                            .leftJoin(menu).on(reservationMenu.menu.id.eq(menu.id))
                            .join(menu.menuCategory,menuCategory)
                            .where(reservationMenu.reservation.id.eq(result.getReservationId()))
                            .fetch();
                    Map<String,List<String>> groupByCategory=menus.stream()
                            .collect(Collectors.groupingBy(
                                    tuple -> tuple.get(menuCategory.name),
                                    Collectors.mapping(tuple -> tuple.get(menu.menuName),Collectors.toList())
                            ));
                    List<ReserveResponseDTO.menuInfo> menuInfos=groupByCategory.entrySet().stream()
                            .map(entry ->
                               ReserveResponseDTO.menuInfo.builder()
                                       .menuCategory(entry.getKey())
                                       .menuNames(entry.getValue())
                                       .build())
                            .toList();
                    result.setReservationMenus(menuInfos);
                }
        );
        if(now) return results.stream().sorted(Comparator.comparing(ReserveResponseDTO.myPageReservationsResultDTO::getDate).thenComparing(ReserveResponseDTO.myPageReservationsResultDTO::getTime)).toList();
        return results.stream().sorted(Comparator.comparing(ReserveResponseDTO.myPageReservationsResultDTO::getDate)
                .thenComparing(ReserveResponseDTO.myPageReservationsResultDTO::getTime).reversed()
        ).toList();
    }

    private BooleanExpression isValidReservationSchedule(LocalDateTime now){
        return reservationSchedule.workDate.after(now.toLocalDate())
                .or(reservationSchedule.workDate.eq(now.toLocalDate()).and(reservationSchedule.startTime.after(now.toLocalTime())));
    }

    private BooleanExpression isReservationRequestInTheMonth(LocalDate startDate,LocalDate endDate){
        return reservationSchedule.workDate.between(startDate, endDate);
    }
    private BooleanExpression isReservationSchInTimeRange(List<ReserveCommandService.TimeRange> timeRanges) {
        BooleanExpression timeRangeCondition = null;
        for (ReserveCommandService.TimeRange timeRange : timeRanges) {
            BooleanExpression condition = reservationSchedule.startTime.goe(timeRange.getStartTime())
                    .and(reservationSchedule.endTime.loe(timeRange.getEndTime()));
            if (timeRangeCondition == null) {
                timeRangeCondition = condition;
            } else {
                timeRangeCondition = timeRangeCondition.or(condition);
            }
        }
        return timeRangeCondition;
    }
    private BooleanExpression isShopInAddressRange(List<ReserveCommandService.AddressRange> addressRanges){
        if(addressRanges==null||addressRanges.isEmpty()){
            return null;
        }
        BooleanExpression addressRangeCondition=null;
        for (ReserveCommandService.AddressRange range:addressRanges){
            BooleanExpression condition=shop.address.firstAddress.eq(range.getFirstAddress())
                    .and(shop.address.secondAddress.eq(range.getSecondAddress()));
            if (!"전체".equals(range.getThirdAddress())){
                condition=condition.and(shop.address.thirdAddress.eq(range.getThirdAddress()));
            }
            if (addressRangeCondition==null) addressRangeCondition=condition;
            else addressRangeCondition=condition.or(condition);
        }
        return addressRangeCondition;
    }
    private BooleanExpression hasToClassifyCategory(Long categoryId){
        return categoryId>=0? shop.category.id.eq(categoryId):null;
    }
    private BooleanExpression forNowReservations(boolean isNow){
        return isNow?isValidReservationSchedule(LocalDateTime.now()).and(reservation.isRefused.eq(false)):null;
    }
}