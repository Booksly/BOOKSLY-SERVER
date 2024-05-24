package kyonggi.bookslyserver.domain.notice.service;

import jakarta.transaction.Transactional;
import kyonggi.bookslyserver.domain.notice.dto.NoticeResponseDTO;
import kyonggi.bookslyserver.domain.notice.entity.ShopOwnerNotice;
import kyonggi.bookslyserver.domain.notice.entity.UserNotice;
import kyonggi.bookslyserver.domain.notice.repository.ShopOwnerNoticeRepository;
import kyonggi.bookslyserver.domain.notice.repository.UserNoticeRepository;
import kyonggi.bookslyserver.domain.reservation.entity.Reservation;
import kyonggi.bookslyserver.global.error.ErrorCode;
import kyonggi.bookslyserver.global.error.exception.EntityNotFoundException;
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
    public String deleteNotices(Long noticeId,boolean isUser){
        if (isUser) userNoticeRepository.findById(noticeId).orElseThrow(()-> new EntityNotFoundException(ErrorCode.ENTITY_NOT_FOUND))
                .setDeleted(true);
        else shopOwnerNoticeRepository.findById(noticeId).orElseThrow(()->new EntityNotFoundException(ErrorCode.ENTITY_NOT_FOUND))
                .setDeleted(true);

        return "알림이 삭제되었습니다.";
    }

}