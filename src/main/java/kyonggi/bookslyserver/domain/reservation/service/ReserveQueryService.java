package kyonggi.bookslyserver.domain.reservation.service;

import org.springframework.transaction.annotation.Transactional;
import kyonggi.bookslyserver.domain.reservation.entity.ReservationSchedule;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ReserveQueryService {
    
    public List<ReservationSchedule> sortSchedulesByStartTimeThenRating(List<ReservationSchedule> eventReservationSchedules) {
        return eventReservationSchedules.stream()
                .sorted(Comparator
                        .comparing(ReservationSchedule::getStartTime)
                        .thenComparing(rs -> rs.getShop().getRatingByReview(), Comparator.reverseOrder())
                        .thenComparing(rs -> rs.getShop().getCreateDate()))
                .toList();
    }
}
