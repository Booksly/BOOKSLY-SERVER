package kyonggi.bookslyserver.domain.notice.service;

import jakarta.transaction.Transactional;
import kyonggi.bookslyserver.domain.notice.dto.NoticeResponseDTO;
import kyonggi.bookslyserver.domain.notice.repository.ShopOwnerNoticeRepository;
import kyonggi.bookslyserver.domain.notice.repository.UserNoticeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class NoticeCommandService {
    private final UserNoticeRepository userNoticeRepository;
    private final ShopOwnerNoticeRepository shopOwnerNoticeRepository;
    public List<NoticeResponseDTO.RefusedReservationsResultDTO> getRefusedReservationsNotices(Long userID){
        return userNoticeRepository.getRefusedReservationsNotices(userID);
    }
    public List<NoticeResponseDTO.ConfirmedReservationsResultDTO> getConfirmedReservationsNotices(Long userID){
        return userNoticeRepository.getConfirmedReservationsNotices(userID);
    }
    public List<NoticeResponseDTO.TodoReservationsResultDTO> getTodoReservationsNotices(Long userId){
        return userNoticeRepository.getTodoReservationsNotices(userId);
    }
    public List<NoticeResponseDTO.CanceledReservationsResultDTO> getCanceledReservationsNotices(Long shopId){
        return shopOwnerNoticeRepository.getCanceledReservationsNotices(shopId);
    }
    public List<NoticeResponseDTO.ReservationRequestsResultDTO> getReservationRequestsNotices(Long shopId){
        return shopOwnerNoticeRepository.getReservationRequestsNotices(shopId);
    }
}