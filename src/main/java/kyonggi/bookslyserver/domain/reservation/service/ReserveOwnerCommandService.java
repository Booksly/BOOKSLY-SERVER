package kyonggi.bookslyserver.domain.reservation.service;

import jakarta.transaction.Transactional;
import kyonggi.bookslyserver.domain.reservation.dto.ReserveRequestDTO;
import kyonggi.bookslyserver.domain.reservation.dto.ReserveResponseDTO;
import kyonggi.bookslyserver.domain.reservation.entity.Reservation;
import kyonggi.bookslyserver.domain.reservation.repository.ReservationRepository;
import kyonggi.bookslyserver.global.error.ErrorCode;
import kyonggi.bookslyserver.global.error.exception.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class ReserveOwnerCommandService {
    private final ReservationRepository reservationRepository;

    /**
     * 가게 주인 용도- 예약 확인 슬롯
     */
    public List<ReserveResponseDTO.getReservationRequestResultDTO> getReservationRequest(Long shopId){
        return reservationRepository.getReservationRequest(shopId);
    }
    public List<ReserveResponseDTO.getReservationRequestResultDTO> getImminentReservationRequest(Long shopId){
        return reservationRepository.getImminentReservationRequest(shopId);
    }

    public List<ReserveResponseDTO.getDatesWithResReqResultDTO> getDatesWithResRequest(int year,int month,Long shopId){
        return reservationRepository.getDatesWithResReqResultDTO(year,month,shopId);
    }

    public String confirmReservationRequest(Long reservationId){
        Reservation reservation=reservationRepository.findById(reservationId)
                .orElseThrow(()->new EntityNotFoundException());
        reservation.setConfirmed(true);
        reservationRepository.save(reservation);
        return "예약이 확정되었습니다";
    }
    public String refuseReservationRequest(Long reservationId, ReserveRequestDTO.refuseReasonRequestDTO requestDTO){
        Reservation reservation=reservationRepository.findById(reservationId)
                .orElseThrow(()->new EntityNotFoundException());
        reservation.setRefused(true);
        reservation.setRefuseReason(requestDTO.getRefuseReason());
        reservationRepository.save(reservation);
        return "예약이 거절되었습니다";
    }
    /**
     * 가게 주인 용도- 직원별 오늘 예약 전체 조회
     */
    public List<ReserveResponseDTO.getTodayReservationsResultDTO> getTodayReservations(LocalDate today,Long shopId, Long employeeId){
        return null;
    }
}
