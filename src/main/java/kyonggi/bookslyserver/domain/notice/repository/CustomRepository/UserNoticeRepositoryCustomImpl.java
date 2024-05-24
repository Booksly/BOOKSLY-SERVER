package kyonggi.bookslyserver.domain.notice.repository.CustomRepository;

import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.StringTemplate;
import com.querydsl.jpa.impl.JPAQueryFactory;
import kyonggi.bookslyserver.domain.notice.constant.NoticeType;
import kyonggi.bookslyserver.domain.notice.dto.NoticeResponseDTO;
import kyonggi.bookslyserver.domain.notice.entity.QUserNotice;
import kyonggi.bookslyserver.domain.reservation.entity.QReservation;
import kyonggi.bookslyserver.domain.reservation.entity.QReservationSchedule;
import kyonggi.bookslyserver.domain.shop.entity.Shop.QShop;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
@Repository
@RequiredArgsConstructor
public class UserNoticeRepositoryCustomImpl implements UserNoticeRepositoryCustom{
    private final JPAQueryFactory queryFactory;
    QReservation reservation=QReservation.reservation;
    QReservationSchedule reservationSchedule=QReservationSchedule.reservationSchedule;
    QUserNotice userNotice=QUserNotice.userNotice;
    QShop shop=QShop.shop;
    @Override
    public List<NoticeResponseDTO.RefusedReservationsResultDTO> getRefusedReservationsNotices(Long userID) {
        return queryFactory.select(
                Projections.fields(NoticeResponseDTO.RefusedReservationsResultDTO.class,
                        userNotice.id.as("noticeId"),
                        userNotice.createDate.as("createdTime"),
                        shop.name.as("shopName"),
                        formatReservationTime(reservationSchedule.workDate,reservationSchedule.startTime).as("reservationTime"),
                        reservation.refuseReason.as("refuseReason")
                        ))
                .from(userNotice)
                .join(userNotice.reservation,reservation)
                .join(reservation.reservationSchedule,reservationSchedule)
                .join(reservationSchedule.shop,shop)
                .where(
                        userNotice.noticeType.eq(NoticeType.REFUSE).and(userNotice.isDeleted.isFalse())
                )
                .orderBy(userNotice.createDate.desc())
                .fetch();
    }

    @Override
    public List<NoticeResponseDTO.ConfirmedReservationsResultDTO> getConfirmedReservationsNotices(Long userID) {
        return queryFactory.select(
                Projections.fields(NoticeResponseDTO.ConfirmedReservationsResultDTO.class,
                        userNotice.id.as("noticeId"),
                        userNotice.createDate.as("createdTime"),
                        shop.name.as("shopName"),
                        formatReservationTime(reservationSchedule.workDate,reservationSchedule.startTime).as("reservationTime")
                        ))
                .from(userNotice)
                .join(userNotice.reservation,reservation)
                .join(reservation.reservationSchedule,reservationSchedule)
                .join(reservationSchedule.shop,shop)
                .where(
                        userNotice.noticeType.eq(NoticeType.CONFIRM).and(userNotice.isDeleted.isFalse())
                )
                .orderBy(userNotice.createDate.desc())
                .fetch();
    }

    @Override
    public List<NoticeResponseDTO.TodoReservationsResultDTO> getTodoReservationsNotices(Long userId) {
        return queryFactory.select(
                Projections.fields(NoticeResponseDTO.TodoReservationsResultDTO.class,
                        reservation.reservationSchedule.employee.name.as("employeeName"),
                        formatReservationTime(reservationSchedule.workDate,reservationSchedule.startTime).as("reservationTime")
                        ))
                .from(reservation)
                .join(reservation.reservationSchedule,reservationSchedule)
                .where(reservation.user.id.eq(userId),
                        reservation.isCanceled.isFalse().and(reservation.isConfirmed.isTrue()),
                        isFutureReservationSchedule(LocalDateTime.now())
                        )
                .orderBy(
                        reservationSchedule.workDate.asc(),
                        reservationSchedule.startTime.asc()
                )
                .fetch();
    }
    private BooleanExpression isFutureReservationSchedule(LocalDateTime now){
        return reservationSchedule.workDate.after(now.toLocalDate())
                .or(reservationSchedule.workDate.eq(now.toLocalDate()).and(reservationSchedule.startTime.after(now.toLocalTime())));
    }
    private StringTemplate formatReservationTime(Expression<?> workDate, Expression<?> startTime) {
        return Expressions.stringTemplate("CONCAT({0}, ' ', DATE_FORMAT({1}, '%H:%i:%s'))", workDate, startTime);
    }
}
