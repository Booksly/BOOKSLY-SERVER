package kyonggi.bookslyserver.domain.reservation.service;

import jakarta.transaction.Transactional;
import kyonggi.bookslyserver.domain.notice.constant.NoticeType;
import kyonggi.bookslyserver.domain.notice.entity.UserNotice;
import kyonggi.bookslyserver.domain.notice.repository.UserNoticeRepository;
import kyonggi.bookslyserver.domain.reservation.converter.ReservationConverter;
import kyonggi.bookslyserver.domain.reservation.dto.ReserveRequestDTO;
import kyonggi.bookslyserver.domain.reservation.dto.ReserveResponseDTO;
import kyonggi.bookslyserver.domain.reservation.entity.Reservation;
import kyonggi.bookslyserver.domain.reservation.entity.ReservationSchedule;
import kyonggi.bookslyserver.domain.reservation.entity.ReservationSetting;
import kyonggi.bookslyserver.domain.reservation.repository.ReservationRepository;
import kyonggi.bookslyserver.domain.reservation.repository.ReservationScheduleRepository;
import kyonggi.bookslyserver.domain.reservation.repository.ReservationSettingRepository;
import kyonggi.bookslyserver.domain.shop.entity.Employee.Employee;
import kyonggi.bookslyserver.domain.shop.entity.Employee.WorkSchedule;
import kyonggi.bookslyserver.domain.shop.entity.Shop.Shop;
import kyonggi.bookslyserver.domain.shop.repository.EmployeeRepository;
import kyonggi.bookslyserver.domain.shop.repository.ShopRepository;
import kyonggi.bookslyserver.global.error.ErrorCode;
import kyonggi.bookslyserver.global.error.exception.EntityNotFoundException;
import kyonggi.bookslyserver.global.error.exception.InvalidValueException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class ReserveOwnerCommandService {
    private final ReservationRepository reservationRepository;
    private final ShopRepository shopRepository;
    private final EmployeeRepository employeeRepository;
    private final ReservationScheduleRepository reservationScheduleRepository;
    private final ReservationSettingRepository reservationSettingRepository;
    private final UserNoticeRepository userNoticeRepository;
    /**
     *  시간대 수동 마감
     */
    public String closeOrOpenReservationSchedule(Long reservationScheduleId){
        ReservationSchedule reservationSchedule=reservationScheduleRepository.findById(reservationScheduleId)
                .orElseThrow(()->new EntityNotFoundException(ErrorCode.SCHEDULE_NOT_FOUND));
        reservationSchedule.setClosed(!reservationSchedule.isClosed());
        if (reservationSchedule.isClosed()) {
            return "시간대 마감 완료";
        } else {
            return "시간대 오픈 완료";
        }
    }
    /**
     * reservation setting 생성 로직
     */
    public ReserveResponseDTO.reservationSettingResultDTO setReservationSetting(ReserveRequestDTO.reservationSettingRequestDTO request, Long shopId){
        Shop shop=shopRepository.findById(shopId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.ENTITY_NOT_FOUND));

        if ((request.getRegisterMin() == null && request.getRegisterHr() == null)) throw new InvalidValueException(ErrorCode.TIME_SETTING_BAD_REQUEST);
        if((request.isAuto() && request.getMaxCapacity() == null)) throw new InvalidValueException(ErrorCode.AUTO_SETTING_BAD_REQUEST);

        ReservationSetting reservationSetting;
        // 존재 여부 확인
        Optional<ReservationSetting> existingSetting=reservationSettingRepository.findByShop(shop);

        if (existingSetting.isPresent()){
            reservationSetting= ReservationConverter.updateReservationSetting(request,existingSetting.get());
        }
        else {
            reservationSetting= ReservationConverter.toReservationSetting(request);
            reservationSetting.setShop(shop);
        }
        return ReservationConverter.toReservationSettingResultDTO(reservationSettingRepository.save(reservationSetting));
    }
    /**
     * 직원별 reservation Schedule 생성 로직
     */
    public String createEmployeeReservationSchedule(Long employeeId){

        Employee employee=employeeRepository.findById(employeeId).orElseThrow(()->new EntityNotFoundException(ErrorCode.ENTITY_NOT_FOUND));
        Shop shop=shopRepository.findById(employee.getShop().getId()).orElseThrow(()-> new EntityNotFoundException(ErrorCode.ENTITY_NOT_FOUND));
        Optional<ReservationSetting> existingSetting=reservationSettingRepository.findByShop(shop);
        List<WorkSchedule> workSchedules=employee.getWorkSchedules();
        LocalDate startDate=LocalDate.now();

        if (existingSetting.isPresent()){
            ReservationSetting reservationSetting=existingSetting.get();
            /*
             * 가게 기본 설정 불러오기
             */
            Integer registerMin=reservationSetting.getRegisterMin();
            Integer registerHr=reservationSetting.getRegisterHr();
            if (registerHr==null) registerHr=0;
            if (registerMin==null) registerMin=0;

            Duration interval= Duration.ofHours(registerHr).plusMinutes(registerMin);
            int cycle=reservationSetting.getReservationCycle();
            LocalDate endDate=startDate.plusDays(cycle-1);

            boolean isAutoConfirmed=reservationSetting.isAutoConfirmation();

            for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {
                DayOfWeek dayOfWeek=date.getDayOfWeek();
                final LocalDate finalDate=date;
                workSchedules.stream()
                        .filter(ws-> !ws.isDayOff()&&ws.getDayOfWeek().equals(dayOfWeek))
                        .forEach(
                                ws ->{
                                    LocalTime startTime=ws.getStartTime();
                                    LocalTime endTime=ws.getEndTime();

                                    while (startTime.plus(interval).isBefore(endTime)||startTime.plus(interval).equals(endTime)){
                                        reservationScheduleRepository.save(ReservationConverter.toReservationSchedule(startTime,finalDate,interval,employee,shop,isAutoConfirmed));
                                        startTime=startTime.plus(interval);
                                    }
                                }
                        );
            }
        }else throw new EntityNotFoundException(ErrorCode.SETTING_NOT_FOUND);
        return "생성 완료!";
    }


    /**
     * 가게 주인 용도- 예약 확인 슬롯, 개별 페이지- 예약 요청 상세 조회
     */
    public List<ReserveResponseDTO.getReservationRequestResultDTO> getReservationRequest(Long shopId){
        return reservationRepository.getReservationRequest(shopId);
    }
    public List<ReserveResponseDTO.getReservationRequestResultDTO> getImminentReservationRequest(Long shopId){
        return reservationRepository.getImminentReservationRequest(shopId);
    }
    public List<ReserveResponseDTO.getReservationRequestDetailsResultDTO> getReservationRequestDetails(Long shopId){
        return reservationRepository.getReservationRequestDetails(shopId);
    }
    public List<ReserveResponseDTO.getReservationRequestDetailsResultDTO> getImminentReservationRequestDetails(Long shopId){
        return reservationRepository.getImminentReservationRequestDetails(shopId);
    }
    public List<ReserveResponseDTO.getDatesWithResReqResultDTO> getDatesWithResRequest(int year,int month,Long shopId){
        return reservationRepository.getDatesWithResReqResultDTO(year,month,shopId);
    }

    public String confirmReservationRequest(Long reservationId){
        Reservation reservation=reservationRepository.findById(reservationId)
                .orElseThrow(()->new EntityNotFoundException(ErrorCode.RESERVATION_NOT_FOUND));
        reservation.setConfirmed(true);
        reservationRepository.save(reservation);

        userNoticeRepository.save(
                UserNotice.builder()
                        .noticeType(NoticeType.CONFIRM)
                        .reservation(reservation)
                        .user(reservation.getUser())
                        .build()
        );
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
        /**
         * 거절된 예약 알림 객체 생성
         */
        userNoticeRepository.save(
          UserNotice.builder()
                  .noticeType(NoticeType.REFUSE)
                  .reservation(reservation)
                  .user(reservation.getUser())
                  .build()
        );
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
    /**
     * 예약 확인- 확정 예약 상세 조회
     */
    public List<ReserveResponseDTO.getTodayReservationsDetailsResultDTO> getTodayReservationsDetails(LocalDate today, Long employeeId){
        return reservationRepository.getTodayReservationsDetails(today, employeeId);
    }
    public ReserveResponseDTO.getReservationsDetailsOfDateResultDTO getReservationsOfDateDetails(LocalDate date, Long employeeId){
        return ReserveResponseDTO.getReservationsDetailsOfDateResultDTO.builder()
                .date(date)
                .employeeName(employeeRepository.findById(employeeId)
                        .orElseThrow(()->new EntityNotFoundException(ErrorCode.EMPLOYEE_NOT_FOUND)).getName())
                .reservationList(reservationRepository.getTodayReservationsDetails(date, employeeId))
                .build();
    }
    public ReserveResponseDTO.getReservationScheduleOfDateResultDTO getReservationScheduleOfDate(LocalDate date, Long employeeId){
        return ReserveResponseDTO.getReservationScheduleOfDateResultDTO.builder()
                .date(date)
                .employeeName(employeeRepository.findById(employeeId)
                        .orElseThrow(()->new EntityNotFoundException(ErrorCode.EMPLOYEE_NOT_FOUND)).getName())
                .scheduleList(reservationRepository.getTodayReservationSchedules(date, employeeId))
                .build();
    }

}
