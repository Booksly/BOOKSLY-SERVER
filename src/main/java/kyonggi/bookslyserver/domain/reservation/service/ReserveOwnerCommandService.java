package kyonggi.bookslyserver.domain.reservation.service;

import jakarta.transaction.Transactional;
import kyonggi.bookslyserver.domain.reservation.dto.ReserveResponseDTO;
import kyonggi.bookslyserver.domain.reservation.repository.ReservationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class ReserveOwnerCommandService {
    private final ReservationRepository reservationRepository;
    public List<ReserveResponseDTO.getReservationRequestResultDTO> getReservationRequest(Long shopId){
        return reservationRepository.getReservationRequest(shopId);
    }
    public List<ReserveResponseDTO.getReservationRequestResultDTO> getImminentReservationRequest(Long shopId){
        return reservationRepository.getImminentReservationRequest(shopId);
    }

    public List<ReserveResponseDTO.getDatesWithResReqResultDTO> getDatesWithResRequest(int year,int month,Long shopId){
        return reservationRepository.getDatesWithResReqResultDTO(year,month,shopId);
    }
}
