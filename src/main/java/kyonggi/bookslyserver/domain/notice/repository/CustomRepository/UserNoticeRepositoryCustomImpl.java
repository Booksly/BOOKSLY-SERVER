package kyonggi.bookslyserver.domain.notice.repository.CustomRepository;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
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
    public List<NoticeResponseDTO.refusedReservationsResultDTO> getRefusedReservationsNotices(Long userID) {
        List<NoticeResponseDTO.refusedReservationsResultDTO> results= queryFactory.select(
                Projections.fields(NoticeResponseDTO.refusedReservationsResultDTO.class,
                        userNotice.createDate.as("createdTime"),
                        shop.name.as("shopName"),
                        Expressions.stringTemplate("CONCAT({0},'T',{1})",
                                reservationSchedule.workDate,
                                reservationSchedule.startTime
                                ).as("reservationTime"),
                        reservationSchedule.workDate, reservationSchedule.startTime,
                        reservation.refuseReason.as("refuseReason")
                        ))
                .from(userNotice)
                .join(userNotice.reservation,reservation)
                .join(reservation.reservationSchedule,reservationSchedule)
                .join(reservationSchedule.shop,shop)
                .where(
                        reservation.isRefused.isTrue()
                )
                .orderBy(userNotice.createDate.desc())
                .fetch();
        return results;
    }
}
