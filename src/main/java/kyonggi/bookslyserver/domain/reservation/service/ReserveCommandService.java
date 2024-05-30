package kyonggi.bookslyserver.domain.reservation.service;

import jakarta.transaction.Transactional;
import kyonggi.bookslyserver.domain.notice.constant.NoticeType;
import kyonggi.bookslyserver.domain.notice.entity.ShopOwnerNotice;
import kyonggi.bookslyserver.domain.notice.repository.ShopOwnerNoticeRepository;
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
import kyonggi.bookslyserver.domain.shop.repository.EmployeeMenuRepository;
import kyonggi.bookslyserver.domain.shop.repository.EmployeeRepository;
import kyonggi.bookslyserver.domain.shop.repository.MenuRepository;
import kyonggi.bookslyserver.domain.user.repository.UserRepository;
import kyonggi.bookslyserver.global.error.ErrorCode;
import kyonggi.bookslyserver.global.error.exception.EntityNotFoundException;
import kyonggi.bookslyserver.global.error.exception.InvalidValueException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
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
    private final ShopOwnerNoticeRepository shopOwnerNoticeRepository;
    private final MenuRepository menuRepository;
    @AllArgsConstructor
    @Getter
    public static class TimeRange{
        LocalTime startTime;
        LocalTime endTime;
    }
    @AllArgsConstructor
    @Getter
    public static class AddressRange {
        String firstAddress;
        String secondAddress;
        String thirdAddress;
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

        ReservationSchedule reservationSchedule=reservationScheduleRepository.findById(requestDTO.getReservationScheduleId())
                .orElseThrow(()->new EntityNotFoundException(ErrorCode.SCHEDULE_NOT_FOUND));
        Employee employee=employeeRepository.findById(reservationSchedule.getEmployee().getId()).orElseThrow(()->new EntityNotFoundException(ErrorCode.EMPLOYEE_NOT_FOUND));

        validateReservationMenus(employee,requestDTO.getReservationMenus());

        /**
         *  가격 계산
         */
        int totalPrice= 0;
        for (ReserveRequestDTO.reservationMenuRequestDTO menu: requestDTO.getReservationMenus()){
            int menuPrice=menu.getPrice();
            System.out.println(menu.isEvent());
            if (menu.isEvent()){
                int dc=menu.getDiscount();
                double dcRatio=dc/100.0;
                int dcPrice=(int) (menuPrice-(menuPrice*dcRatio));
                totalPrice+=dcPrice;
            }
            else totalPrice+=menuPrice;
        }

        /**
         * Reservation 생성
         */

        Reservation newReservation=Reservation.builder()
                .price(totalPrice)
                .reservationSchedule(reservationSchedule)
                .inquiry(requestDTO.getInquiry())
                .timeEventTitle(requestDTO.getTimeEventTitle())
                .user(userRepository.findById(userId).orElseThrow(()->new EntityNotFoundException(ErrorCode.MEMBER_NOT_FOUND)))
                .reservationMenus(new ArrayList<>())
                .isConfirmed(reservationSchedule.isAutoConfirmed())
                .build();
        reservationSchedule.getReservations().add(newReservation);
        reservationRepository.save(newReservation);
        /**
         *  Reservation Menu 생성
         */
        requestDTO.getReservationMenus().forEach(menu->{

            newReservation.getReservationMenus().add(
                    reservationMenuRepository.save(
                            ReservationMenu.builder()
                                    .reservation(newReservation)
                                    .menu(menuRepository.findById(menu.getMenuId()).orElseThrow(()->new EntityNotFoundException(ErrorCode.MENU_NOT_FOUND)))
                                    .build()
                    )
            );
        });

        if(reservationSchedule.isAutoConfirmed()) autoReservationClose(reservationSchedule);
        /**
         * 예약 요청 알림 생성
         */

        shopOwnerNoticeRepository.save(ShopOwnerNotice.builder()
                .reservation(newReservation)
                .noticeType(NoticeType.REQUEST)
                .shopOwner(reservationSchedule.getShop().getShopOwner())
                .build());
        return ReservationConverter.toCreateReservationResultDTO(newReservation);
    }
    /**
     * 메뉴 검증
     */
    public void validateReservationMenus(Employee employee,List<ReserveRequestDTO.reservationMenuRequestDTO> reservationMenus){
        Set<Long> menuIds=new HashSet<>();
        for (ReserveRequestDTO.reservationMenuRequestDTO menu:reservationMenus){
            if (!menuIds.add(menu.getMenuId())){
                throw new InvalidValueException(ErrorCode.MENUS_ARE_DUPLICATED);
            }
            if (menu.isEvent()){
                if (menu.getDiscount()==null||menu.getDiscount()==0){
                    throw new InvalidValueException(ErrorCode.DISCOUNT_SETTING_BAD_REQUEST);
                }
            }
            System.out.println(employee.getId()+" "+menu.getMenuId());
            if (!employeeMenuRepository.findByEmployeeAndMenu(employee,
                    menuRepository.findById(menu.getMenuId()).orElseThrow(()->new EntityNotFoundException(ErrorCode.MENU_NOT_FOUND))).isPresent()){
                throw new InvalidValueException(ErrorCode.EMPLOYEES_MENUS_NOT_MATCHING);
            }
        }
    }
    /**
     * 마이페이지 전체 예약 조회, 카테고리별
     */
    public List<ReserveResponseDTO.myPageReservationsResultDTO> getAllReservationRecords(Long userId){
        return reservationRepository.getAllReservationRecords(userId,(long)-1,false);
    }
    public List<ReserveResponseDTO.myPageReservationsResultDTO> getAllReservationRecordsByCategory(Long userId,Long categoryId){
        return reservationRepository.getAllReservationRecords(userId,categoryId,false);
    }
    /**
     * 마이페이지 현재 예약 조회, 카테고리별
     */
    public List<ReserveResponseDTO.myPageReservationsResultDTO> getNowReservationRecords(Long userId){
        return reservationRepository.getAllReservationRecords(userId,(long)-1,true);
    }
    public List<ReserveResponseDTO.myPageReservationsResultDTO> getNowReservationRecordsByCategory(Long userId,Long categoryId){
        return reservationRepository.getAllReservationRecords(userId,categoryId,true);
    }
    /**
     * 예약 취소하기
     */
    public String cancelReservation(Long reservationId){
        Reservation reservation=reservationRepository.findById(reservationId).orElseThrow(()-> new EntityNotFoundException(ErrorCode.RESERVATION_NOT_FOUND));
        reservation.setCanceled(true);
        reservationRepository.save(reservation);

        ReservationSchedule reservationSchedule=reservationScheduleRepository.findById(reservation.getReservationSchedule().getId()).orElseThrow(()->new EntityNotFoundException(ErrorCode.ENTITY_NOT_FOUND));
        reservationSchedule.getReservations().remove(reservation);

        if (reservationSchedule.isAutoConfirmed()&&reservationSchedule.isClosed()){
            reservationSchedule.setClosed(false);
        }

        /**
         * 예약 취소 알림 생성
         */
        shopOwnerNoticeRepository.save(
                ShopOwnerNotice.builder()
                        .noticeType(NoticeType.CANCEL)
                        .reservation(reservation)
                        .shopOwner(reservationSchedule.getShop().getShopOwner())
                        .build()
        );

        return reservation.getUser().getNickname()+"님의 "+"예약 ID"+reservation.getId()+"이(가) 취소되었습니다.";
    }
    public String deleteReservation(Long reservationId){
        Reservation reservation=reservationRepository.findById(reservationId).orElseThrow(()-> new EntityNotFoundException(ErrorCode.RESERVATION_NOT_FOUND));
        reservation.setDeleted(true);
        reservationRepository.save(reservation);
        return reservation.getUser().getNickname()+"님의 "+"예약 ID"+reservation.getId()+"이(가) 삭제되었습니다.";
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
    public List<ReserveResponseDTO.findTodayReservationsResultDTO> findTodayReservation(LocalDate date,List<String> firstAddress,List<String> secondAddress,List<String> thirdAddress,List<LocalTime> startTimes,List<LocalTime> endTimes, List<Long> categories){
        List<TimeRange> timeRanges=createTimeRange(startTimes, endTimes);
        List<AddressRange> addressRanges=createAddressRange(firstAddress, secondAddress, thirdAddress);
        return reservationRepository.findTodayReservations(date,addressRanges,timeRanges,categories);
    }
    public List<ReserveResponseDTO.findTodayReservationsResultDTO> findTodayReservationByDiscount(LocalDate date,List<String> firstAddress,List<String> secondAddress,List<String> thirdAddress,List<LocalTime> startTimes,List<LocalTime> endTimes, List<Long> categories){
        List<TimeRange> timeRanges=createTimeRange(startTimes, endTimes);
        List<AddressRange> addressRanges=createAddressRange(firstAddress, secondAddress, thirdAddress);
        return reservationRepository.findTodayReservationsByDiscount(date,addressRanges,timeRanges,categories);
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
    public static List<AddressRange> createAddressRange(List<String> firstAddress, List<String> secondAddress, List<String> thirdAddress){
        List<AddressRange> addressRanges=new ArrayList<>();
        for (int i=0;i< firstAddress.size();i++){
            String firstAddr = firstAddress.get(i);
            String secondAddr = secondAddress.get(i);
            String thirdAddr = thirdAddress.get(i);
            addressRanges.add(new AddressRange(firstAddr,secondAddr,thirdAddr));
        }
        return addressRanges;
    }
}