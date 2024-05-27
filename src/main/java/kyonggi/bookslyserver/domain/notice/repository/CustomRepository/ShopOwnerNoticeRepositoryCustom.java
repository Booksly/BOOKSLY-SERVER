package kyonggi.bookslyserver.domain.notice.repository.CustomRepository;

import kyonggi.bookslyserver.domain.notice.dto.NoticeResponseDTO;

import java.util.List;

public interface ShopOwnerNoticeRepositoryCustom {
    List<NoticeResponseDTO.CanceledReservationsResultDTO> getCanceledReservationsNotices(Long shopId);
    List<NoticeResponseDTO.ReservationRequestsResultDTO> getReservationRequestsNotices(Long shopId);
}
