package kyonggi.bookslyserver.domain.reservation.repository.CustomRepository;

import kyonggi.bookslyserver.domain.reservation.dto.ReserveResponseDTO;
import kyonggi.bookslyserver.domain.reservation.service.ReserveCommandService;

import java.time.LocalDate;
import java.util.List;

public interface ReservationRepositoryCustom {
    public List<ReserveResponseDTO.getReservationRequestResultDTO> getReservationRequest(Long shopId);
    public List<ReserveResponseDTO.getReservationRequestResultDTO> getImminentReservationRequest(Long shopId);
    public List<ReserveResponseDTO.getDatesWithResReqResultDTO> getDatesWithResReqResultDTO(int year,int month, Long shopId);
    public List<ReserveResponseDTO.getTodayReservationSchedulesResultDTO> getTodayReservationSchedules(LocalDate today, Long employeeId);

    public List<ReserveResponseDTO.getOnlyReservationsOfDateResultDTO> getOnlyReservationsOfDate(LocalDate date, Long employeeId);
    public List<ReserveResponseDTO.getTodayReservationsAllEmpsResultDTO> getTodayReservationsScheduleAllEmps(LocalDate today, Long shopId);
    public List<ReserveResponseDTO.getTodayReservationsDetailsResultDTO> getTodayReservationsDetails(LocalDate today, Long employeeId);
    public List<ReserveResponseDTO.getReservationRequestDetailsResultDTO> getReservationRequestDetails(Long shopId);
    public List<ReserveResponseDTO.getReservationRequestDetailsResultDTO> getImminentReservationRequestDetails(Long shopId);
    public List<ReserveResponseDTO.findTodayReservationsResultDTO> findTodayReservations(LocalDate date, List<ReserveCommandService.TimeRange> timeRanges,List<Long> categories);
    public List<ReserveResponseDTO.findTodayReservationsResultDTO> findTodayReservationsByDiscount(LocalDate date,List<ReserveCommandService.TimeRange> timeRanges,List<Long> categories);
    public List<ReserveResponseDTO.myPageReservationsResultDTO> getAllReservationRecords(Long userId, Long categoryId, boolean now);
}