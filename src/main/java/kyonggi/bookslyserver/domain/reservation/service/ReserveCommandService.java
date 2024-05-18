package kyonggi.bookslyserver.domain.reservation.service;

import jakarta.transaction.Transactional;
import kyonggi.bookslyserver.domain.reservation.converter.ReservationConverter;
import kyonggi.bookslyserver.domain.reservation.dto.ReserveRequestDTO;
import kyonggi.bookslyserver.domain.reservation.dto.ReserveResponseDTO;
import kyonggi.bookslyserver.domain.reservation.entity.Reservation;
import kyonggi.bookslyserver.domain.reservation.entity.ReservationMenu;
import kyonggi.bookslyserver.domain.reservation.entity.ReservationSchedule;
import kyonggi.bookslyserver.domain.reservation.entity.ReservationSetting;
import kyonggi.bookslyserver.domain.reservation.repository.ReservationMenuRepository;
import kyonggi.bookslyserver.domain.reservation.repository.ReservationRepository;
import kyonggi.bookslyserver.domain.reservation.repository.ReservationScheduleRepository;
import kyonggi.bookslyserver.domain.reservation.repository.ReservationSettingRepository;
import kyonggi.bookslyserver.domain.shop.entity.Employee.Employee;
import kyonggi.bookslyserver.domain.shop.entity.Employee.EmployeeMenu;
import kyonggi.bookslyserver.domain.shop.entity.Employee.WorkSchedule;
import kyonggi.bookslyserver.domain.shop.entity.Shop.Shop;
import kyonggi.bookslyserver.domain.shop.repository.EmployeeMenuRepository;
import kyonggi.bookslyserver.domain.shop.repository.EmployeeRepository;
import kyonggi.bookslyserver.domain.shop.repository.ShopRepository;
import kyonggi.bookslyserver.domain.user.repository.UserRepository;
import kyonggi.bookslyserver.global.error.ErrorCode;
import kyonggi.bookslyserver.global.error.exception.EntityNotFoundException;
import kyonggi.bookslyserver.global.error.exception.InvalidValueException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class ReserveCommandService {
    private final ReservationSettingRepository reservationSettingRepository;
    private final EmployeeRepository employeeRepository;
    private final ReservationScheduleRepository reservationScheduleRepository;
    private final EmployeeMenuRepository employeeMenuRepository;
    private final UserRepository userRepository;
    private final ReservationRepository reservationRepository;
    private final ReservationMenuRepository reservationMenuRepository;
    @AllArgsConstructor
    @Getter
    public static class TimeRange{
        LocalTime startTime;
        LocalTime endTime;
    }
    /**
     * 예약 가능 시간대 조회
     */
    public List<ReserveResponseDTO.availableTimesResultDTO> getAvailableReservationTimes(Long employeeId,LocalDate date){
        Employee employee=employeeRepository.findById(employeeId).orElseThrow(()->new EntityNotFoundException(ErrorCode.EMPLOYEE_NOT_FOUND));
        return reservationScheduleRepository.findByEmployeeAndWorkDate(employee,date)
                .stream()
                .map(ReservationConverter::toAvailableTimesResultDTO)
                .collect(Collectors.toList());
    }
    /**
     *  예약하기
     */
    public ReserveResponseDTO.createReservationResultDTO createReservation(Long userId, ReserveRequestDTO.reservationRequestDTO requestDTO){
        if(reservationScheduleRepository.findById(requestDTO.getReservationScheduleId()).get().isClosed()){
            throw new InvalidValueException(ErrorCode.RESERVATION_CLOSED_BAD_REQUEST);
        }

        /**
         *  가격 계산
         */
        int totalPrice= requestDTO.getReservationMenuRequestDTOS().stream()
                .mapToInt(menuDto->{
                    EmployeeMenu employeeMenu=employeeMenuRepository.findById(menuDto.getEmpMenuId()).orElseThrow(()->new EntityNotFoundException(ErrorCode.EMPLOYEE_MENU_NOT_FOUND));
                    return employeeMenu.getMenu().getPrice();
                }).sum();
        if (requestDTO.isEvent()){
            if (requestDTO.getDiscount()==null) throw new InvalidValueException(ErrorCode.DISCOUNT_SETTING_BAD_REQUEST);
            double dc=(100-requestDTO.getDiscount())/100.0;
            totalPrice=(int)(totalPrice*dc);
        }
        /**
         * Reservation 생성
         */

        ReservationSchedule reservationSchedule=reservationScheduleRepository.findById(requestDTO.getReservationScheduleId())
                .orElseThrow(()->new EntityNotFoundException(ErrorCode.SCHEDULE_NOT_FOUND));

        Reservation newReservation=Reservation.builder()
                .price(totalPrice)
                .reservationSchedule(reservationSchedule)
                .inquiry(requestDTO.getInquiry())
                .eventTitle(requestDTO.getEventTitle())
                .user(userRepository.findById(userId).orElseThrow(()->new EntityNotFoundException(ErrorCode.MEMBER_NOT_FOUND)))
                .reservationMenus(new ArrayList<>())
                .isConfirmed(reservationSchedule.isAutoConfirmed())
                .build();
        reservationSchedule.getReservations().add(newReservation);
        reservationRepository.save(newReservation);
        /**
         *  Reservation Menu 생성
         */
        requestDTO.getReservationMenuRequestDTOS().forEach(menuDto -> {
            EmployeeMenu employeeMenu = employeeMenuRepository.findById(menuDto.getEmpMenuId())
                    .orElseThrow(() -> new EntityNotFoundException(ErrorCode.EMPLOYEE_MENU_NOT_FOUND));
            newReservation.getReservationMenus().add(
                    reservationMenuRepository.save(
                    ReservationMenu.builder()
                            .reservation(newReservation)
                            .menu(employeeMenu.getMenu())
                            .build()
            ));
        });

        if(reservationSchedule.isAutoConfirmed()) autoReservationClose(reservationSchedule);

        return ReservationConverter.toCreateReservationResultDTO(newReservation);
    }
    /**
     * 마이페이지 전체 예약 조회
     */
    public List<ReserveResponseDTO.myPageReservationsResultDTO> getAllReservationRecords(Long userId){
        return reservationRepository.getAllReservationRecords(userId,(long)-1);
    }
    /**
     * 마이페이지 전체 예약 조회- 카테고리별
     */
    public List<ReserveResponseDTO.myPageReservationsResultDTO> getAllReservationRecordsByCategory(Long userId,Long categoryId){
        return reservationRepository.getAllReservationRecords(userId,categoryId);
    }
    /**
     * 자동 예약 마감
     */
    public void autoReservationClose(ReservationSchedule reservationSchedule){

        ReservationSetting reservationSetting=reservationSettingRepository.findByShop(reservationSchedule.getShop()).get();
        if (reservationSetting.getMaxCapacity()==reservationSchedule.getReservations().size())
            reservationSchedule.setClosed(true); // 예약 close

        reservationScheduleRepository.save(reservationSchedule);
    }
    /**
     * 당일 예약
     */
    public List<ReserveResponseDTO.findTodayReservationsResultDTO> findTodayReservation(LocalDate date,List<LocalTime> startTimes,List<LocalTime> endTimes, List<Long> categories){
        List<TimeRange> timeRanges=createTimeRange(startTimes, endTimes);
        return reservationRepository.findTodayReservations(date,timeRanges,categories);
    }
    public List<ReserveResponseDTO.findTodayReservationsResultDTO> findTodayReservationByDiscount(LocalDate date,List<LocalTime> startTimes,List<LocalTime> endTimes, List<Long> categories){
        List<TimeRange> timeRanges=createTimeRange(startTimes, endTimes);
        return reservationRepository.findTodayReservationsByDiscount(date,timeRanges,categories);
    }
    /**
     * startTime, endTime to timeRange
     */
    public static List<TimeRange> createTimeRange(List<LocalTime> startTimes, List<LocalTime> endTimes){
        List<TimeRange> timeRanges=new ArrayList<>();
        for (int i=0;i< startTimes.size();i++){
            LocalTime startTime=startTimes.get(i);
            LocalTime endTime=endTimes.get(i);
            timeRanges.add(new TimeRange(startTime,endTime));
        }
        return timeRanges;
    }
}