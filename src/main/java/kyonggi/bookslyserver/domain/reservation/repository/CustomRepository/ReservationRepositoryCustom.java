package kyonggi.bookslyserver.domain.reservation.repository.CustomRepository;

import kyonggi.bookslyserver.domain.reservation.dto.ReserveResponseDTO;

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
}