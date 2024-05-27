package kyonggi.bookslyserver.domain.notice.repository.CustomRepository;

import kyonggi.bookslyserver.domain.notice.dto.NoticeResponseDTO;

import java.util.List;

public interface UserNoticeRepositoryCustom {
    public List<NoticeResponseDTO.RefusedReservationsResultDTO> getRefusedReservationsNotices(Long userID);
    public List<NoticeResponseDTO.ConfirmedReservationsResultDTO> getConfirmedReservationsNotices(Long userID);
    public List<NoticeResponseDTO.TodoReservationsResultDTO> getTodoReservationsNotices(Long userId);
}
