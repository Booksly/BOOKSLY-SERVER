package kyonggi.bookslyserver.domain.reservation.service;

import jakarta.transaction.Transactional;
import kyonggi.bookslyserver.domain.reservation.converter.ReservationConverter;
import kyonggi.bookslyserver.domain.reservation.dto.ReserveRequestDTO;
import kyonggi.bookslyserver.domain.reservation.dto.ReserveResponseDTO;
import kyonggi.bookslyserver.domain.reservation.entity.Reservation;
import kyonggi.bookslyserver.domain.reservation.entity.ReservationMenu;
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
public class ReserveCommandService {
    private final ReservationSettingRepository reservationSettingRepository;
    private final ShopRepository shopRepository;
    private final EmployeeRepository employeeRepository;
    private final ReservationScheduleRepository reservationScheduleRepository;
    private final EmployeeMenuRepository employeeMenuRepository;
    private final UserRepository userRepository;
    private final ReservationRepository reservationRepository;
    private final ReservationMenuRepository reservationMenuRepository;
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
            reservationSetting=ReservationConverter.updateReservationSetting(request,existingSetting.get());
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
                                        reservationScheduleRepository.save(ReservationConverter.toReservationSchedule(startTime,finalDate,interval,employee,shop));
                                        startTime=startTime.plus(interval);
                                    }
                                }
                        );
            }
        }else throw new EntityNotFoundException(ErrorCode.SETTING_NOT_FOUND);
        return "생성 완료!";
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
    public Long createReservation(Long userId, ReserveRequestDTO.reservationRequestDTO requestDTO){
        /**
         *  가격 계산
         */
        int totalPrice= requestDTO.getReservationMenuRequestDTOS().stream()
                .mapToInt(menuDto->{
                    EmployeeMenu employeeMenu=employeeMenuRepository.findById(menuDto.getEmpMenuId()).orElseThrow(()->new EntityNotFoundException(ErrorCode.EMPLOYEE_MENU_NOT_FOUND));
                    return employeeMenu.getMenu().getPrice();
                }).sum();
        if (requestDTO.isEvent()){
            double dc=(100-requestDTO.getDiscount())/100.0;
            totalPrice=(int)(totalPrice*dc);
        }
        Reservation newReservation=Reservation.builder()
                .price(totalPrice)
                .reservationSchedule(reservationScheduleRepository.findById(requestDTO.getReservationScheduleId()).get())
                .inquiry(requestDTO.getInquiry())
                .eventTitle(requestDTO.getEventTitle())
                .user(userRepository.findById(userId).orElseThrow(()->new EntityNotFoundException(ErrorCode.MEMBER_NOT_FOUND)))
                .build();
        reservationRepository.save(newReservation);
        requestDTO.getReservationMenuRequestDTOS().forEach(menuDto -> {
            EmployeeMenu employeeMenu = employeeMenuRepository.findById(menuDto.getEmpMenuId())
                    .orElseThrow(() -> new EntityNotFoundException(ErrorCode.EMPLOYEE_MENU_NOT_FOUND));
            reservationMenuRepository.save(
                    ReservationMenu.builder()
                            .reservation(newReservation)
                            .menu(employeeMenu.getMenu())
                            .build()
            );
        });
        return newReservation.getId();
    }
}