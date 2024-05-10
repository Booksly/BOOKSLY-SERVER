package kyonggi.bookslyserver.domain.reservation.service;

import jakarta.transaction.Transactional;
import kyonggi.bookslyserver.domain.reservation.dto.ReserveRequestDTO;
import kyonggi.bookslyserver.domain.reservation.dto.ReserveResponseDTO;
import kyonggi.bookslyserver.domain.reservation.entity.Reservation;
import kyonggi.bookslyserver.domain.reservation.repository.ReservationRepository;
import kyonggi.bookslyserver.domain.shop.entity.Shop.Shop;
import kyonggi.bookslyserver.domain.shop.repository.ShopRepository;
import kyonggi.bookslyserver.global.error.ErrorCode;
import kyonggi.bookslyserver.global.error.exception.EntityNotFoundException;
import kyonggi.bookslyserver.global.error.exception.InvalidValueException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class ReserveOwnerCommandService {
    private final ReservationRepository reservationRepository;
    private final ShopRepository shopRepository;

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
                .orElseThrow(()->new EntityNotFoundException(ErrorCode.RESERVATION_NOT_FOUND));
        reservation.setConfirmed(true);
        reservationRepository.save(reservation);
        return "예약이 확정되었습니다";
    }
    public String refuseReservationRequest(Long reservationId, ReserveRequestDTO.refuseReasonRequestDTO requestDTO){
        Reservation reservation=reservationRepository.findById(reservationId)
                .orElseThrow(()->new EntityNotFoundException(ErrorCode.RESERVATION_NOT_FOUND));
        if (requestDTO.getRefuseReason()==null){
            throw new InvalidValueException(ErrorCode.REFUSAL_REASON_MISSING);
        }
        reservation.setRefused(true);
        reservation.setRefuseReason(requestDTO.getRefuseReason());
        reservationRepository.save(reservation);
        return "예약이 거절되었습니다";
    }
    /**
     * 가게 주인 용도- 직원별 오늘(또는 특정 날짜) 예약 전체 조회
     */
    public List<ReserveResponseDTO.getTodayReservationSchedulesResultDTO> getTodayReservationSchedules(LocalDate today, Long employeeId){
        return reservationRepository.getTodayReservationSchedules(today, employeeId);
    }
    public List<ReserveResponseDTO.getOnlyReservationsOfDateResultDTO> getTodayReservationsOnly(LocalDate date, Long employeeId){
        return reservationRepository.getOnlyReservationsOfDate(date, employeeId);
    }
    /**
     * 가게 주인 용도- 전체 직원 오늘(또는 특정 날짜) 예약 전체 조회
     */
    public List<ReserveResponseDTO.getTodayReservationsAllEmpsResultDTO> getTodayReservationSchedulesAllEmps(LocalDate today,Long shopId){
        return reservationRepository.getTodayReservationsScheduleAllEmps(today,shopId);
    }
    public List<ReserveResponseDTO.getOnlyReservationsOfDateAllEmpsResultDTO> getOnlyReservationsOfDateAllEmps(LocalDate date,Long shopId){
        Shop shop=shopRepository.findById(shopId)
                .orElseThrow(()->new EntityNotFoundException(ErrorCode.SHOP_NOT_FOUND));
        return shop.getEmployees().stream()
                .map(employee -> {
                  List<ReserveResponseDTO.getOnlyReservationsOfDateResultDTO> resultDTO=reservationRepository.getOnlyReservationsOfDate(date,employee.getId());
                  return ReserveResponseDTO.getOnlyReservationsOfDateAllEmpsResultDTO.builder()
                          .employeeName(employee.getName())
                          .reservationsList(resultDTO)
                          .build();
                })
                .collect(Collectors.toList());
    }
}
