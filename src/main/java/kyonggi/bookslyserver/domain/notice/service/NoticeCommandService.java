package kyonggi.bookslyserver.domain.notice.service;

import jakarta.transaction.Transactional;
import kyonggi.bookslyserver.domain.notice.dto.NoticeResponseDTO;
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
    public List<NoticeResponseDTO.refusedReservationsResultDTO> getRefusedReservationsNotices(Long userID){
        return userNoticeRepository.getRefusedReservationsNotices(userID);
    }
    public List<NoticeResponseDTO.confirmedReservationsResultDTO> getConfirmedReservationsNotices(Long userID){
        return userNoticeRepository.getConfirmedReservationsNotices(userID);
    }
    public List<NoticeResponseDTO.todoReservationsResultDTO> getTodoReservationsNotices(Long userId){
        return userNoticeRepository.getTodoReservationsNotices(userId);
    }
}