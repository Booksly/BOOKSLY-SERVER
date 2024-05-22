package kyonggi.bookslyserver.domain.notice.repository.CustomRepository;

import kyonggi.bookslyserver.domain.notice.dto.NoticeResponseDTO;

import java.util.List;

public interface UserNoticeRepositoryCustom {
    public List<NoticeResponseDTO.refusedReservationsResultDTO> getRefusedReservationsNotices(Long userID);
    public List<NoticeResponseDTO.confirmedReservationsResultDTO> getConfirmedReservationsNotices(Long userID);
    public List<NoticeResponseDTO.todoReservationsResultDTO> getTodoReservationsNotices(Long userId);
}
