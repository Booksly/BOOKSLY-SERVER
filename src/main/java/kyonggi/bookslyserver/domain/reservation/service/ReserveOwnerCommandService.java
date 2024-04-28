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
    public List<ReserveResponseDTO.getReservationRequestResultDTO> getReservationRequest(){
        return reservationRepository.getReservationRequest();
    }
}
