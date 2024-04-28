package kyonggi.bookslyserver.domain.reservation.repository.CustomRepository;

import kyonggi.bookslyserver.domain.reservation.dto.ReserveResponseDTO;

import java.util.List;

public interface ReservationRepositoryCustom {
    public List<ReserveResponseDTO.getReservationRequestResultDTO> getReservationRequest(Long shopId);
    public List<ReserveResponseDTO.getReservationRequestResultDTO> getImminentReservationRequest(Long shopId);

}