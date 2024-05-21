package kyonggi.bookslyserver.domain.notice.service;

import jakarta.transaction.Transactional;
import kyonggi.bookslyserver.domain.notice.dto.NoticeResponseDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class NoticeCommandService {
    public List<NoticeResponseDTO.refusedReservationsResultDTO> getRefusedReservationsNotices(Long userID){
        return null;
    }

}