package kyonggi.bookslyserver.domain.notice.repository.CustomRepository;

import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.StringTemplate;
import com.querydsl.jpa.impl.JPAQueryFactory;
import kyonggi.bookslyserver.domain.notice.constant.NoticeType;
import kyonggi.bookslyserver.domain.notice.dto.NoticeResponseDTO;
import kyonggi.bookslyserver.domain.notice.entity.QShopOwnerNotice;
import kyonggi.bookslyserver.domain.reservation.entity.QReservation;
import kyonggi.bookslyserver.domain.reservation.entity.QReservationSchedule;
import kyonggi.bookslyserver.domain.shop.entity.Shop.QShop;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
@RequiredArgsConstructor

public class ShopOwnerNoticeRepositoryCustomImpl implements ShopOwnerNoticeRepositoryCustom{
    private final JPAQueryFactory queryFactory;
    QShopOwnerNotice shopOwnerNotice=QShopOwnerNotice.shopOwnerNotice;
    QShop shop=QShop.shop;
    QReservationSchedule reservationSchedule=QReservationSchedule.reservationSchedule;
    QReservation reservation=QReservation.reservation;
    @Override
    public List<NoticeResponseDTO.CanceledReservationsResultDTO> getCanceledReservationsNotices(Long shopId) {
        return queryFactory.select(
                Projections.fields(NoticeResponseDTO.CanceledReservationsResultDTO.class,
                        shopOwnerNotice.id.as("noticeId"),
                        shopOwnerNotice.createDate.as("createdTime"),
                        shop.name.as("shopName"),
                        formatReservationTime(reservationSchedule.workDate,reservationSchedule.startTime).as("reservationTime")
                        ))
                .from(shopOwnerNotice)
                .join(shopOwnerNotice.reservation,reservation)
                .join(reservation.reservationSchedule,reservationSchedule)
                .join(reservationSchedule.shop,shop)
                .where(
                        shopOwnerNotice.noticeType.eq(NoticeType.CANCEL).and(shopOwnerNotice.isDeleted.isFalse()),
                        reservation.isCanceled.isTrue(),
                        shop.id.eq(shopId)
                )
                .orderBy(shopOwnerNotice.createDate.desc())
                .fetch();
    }

    @Override
    public List<NoticeResponseDTO.ReservationRequestsResultDTO> getReservationRequestsNotices(Long shopId) {
        return queryFactory.select(
                Projections.fields(NoticeResponseDTO.ReservationRequestsResultDTO.class,
                        shopOwnerNotice.id.as("noticeId"),
                        shopOwnerNotice.createDate.as("createdTime"),
                        shop.name.as("shopName"),
                        formatReservationTime(reservationSchedule.workDate,reservationSchedule.startTime).as("reservationTime")
                        ))
                .from(shopOwnerNotice)
                .join(shopOwnerNotice.reservation,reservation)
                .join(reservation.reservationSchedule,reservationSchedule)
                .join(reservationSchedule.shop,shop)
                .where(
                        shopOwnerNotice.noticeType.eq(NoticeType.REQUEST).and(shopOwnerNotice.isDeleted.isFalse()),
                        reservation.isCanceled.isFalse(),
                        shop.id.eq(shopId)
                )
                .orderBy(shopOwnerNotice.createDate.desc())
                .fetch();
    }

    private StringTemplate formatReservationTime(Expression<?> workDate, Expression<?> startTime) {
        return Expressions.stringTemplate("CONCAT({0}, ' ', DATE_FORMAT({1}, '%H:%i:%s'))", workDate, startTime);
    }
}
