package kyonggi.bookslyserver.domain.event.service;

import kyonggi.bookslyserver.domain.event.dto.request.CreateTimeEventsRequestDto;
import kyonggi.bookslyserver.domain.event.dto.response.CreateTimeEventsResponseDto;
import kyonggi.bookslyserver.domain.event.entity.timeEvent.*;
import kyonggi.bookslyserver.domain.event.repository.TimeEventRepository;
import kyonggi.bookslyserver.domain.event.repository.EmployTimeEventScheduleRepository;
import kyonggi.bookslyserver.domain.reservation.entity.ReservationSchedule;
import kyonggi.bookslyserver.domain.reservation.repository.ReservationScheduleRepository;
import kyonggi.bookslyserver.domain.shop.entity.Employee.Employee;
import kyonggi.bookslyserver.domain.shop.entity.Shop.Shop;
import kyonggi.bookslyserver.domain.shop.repository.EmployeeRepository;
import kyonggi.bookslyserver.domain.shop.repository.MenuRepository;
import kyonggi.bookslyserver.domain.shop.repository.ShopRepository;
import kyonggi.bookslyserver.domain.shop.service.ShopService;
import kyonggi.bookslyserver.global.error.ErrorCode;
import kyonggi.bookslyserver.global.error.exception.BusinessException;
import kyonggi.bookslyserver.global.error.exception.ConflictException;
import kyonggi.bookslyserver.global.error.exception.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

import static kyonggi.bookslyserver.global.error.ErrorCode.*;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class TimeEventCommandService {

    private final EmployeeRepository employeeRepository;
    private final ShopRepository shopRepository;
    private final MenuRepository menuRepository;
    private final TimeEventRepository timeEventRepository;
    private final EmployTimeEventScheduleRepository employTimeEventScheduleRepository;
    private final ReservationScheduleRepository reservationScheduleRepository;
    private final ShopService shopService;

    public CreateTimeEventsResponseDto createTimeEvents(Long ownerId, CreateTimeEventsRequestDto requestDto) {

        Shop shop = shopService.findShop(ownerId, requestDto.shopId());

        validateRepeatSettings(requestDto.isRepeat(), requestDto.isDateRepeat(), requestDto.isDayOfWeekRepeat());

        List<DayOfWeek> dayOfWeeks = createRepeatDayOfWeeks(requestDto);

        TimeEvent timeEvent = createTimeEvent(requestDto, shop, dayOfWeeks);
        timeEventRepository.save(timeEvent);

        createTimeEventSchedulesForEmployee(requestDto,dayOfWeeks,timeEvent);

        setMenuAndTimeEventToTimeEventMenu(requestDto, timeEvent);
        setEmployeeAndTimeEventToEmployeeTimeEvent(requestDto, timeEvent);

        return CreateTimeEventsResponseDto.of(timeEvent);
    }

    private void validateRepeatSettings(boolean isRepeat, boolean isDateRepeat, boolean isDayOfWeekRepeat) {

        // true, true, true 불가
        if (isRepeat && isDateRepeat && isDayOfWeekRepeat)
            throw new BusinessException(DUPLICATED_REPEAT_SETTING_BAD_REQUEST);

        // true, false, false 불가
        if (isRepeat && !isDateRepeat && !isDayOfWeekRepeat)
            throw new BusinessException(INCOMPLETE_REPEAT_SETTING_BAD_REQUEST);

        // isRepeat가 false이면 isDateRepeat, isDayOfWeekRepeat는 모두 false
        if (!isRepeat && (isDateRepeat || isDayOfWeekRepeat))
            throw new BusinessException(BAD_REQUEST);

    }

    private List<DayOfWeek> createRepeatDayOfWeeks(CreateTimeEventsRequestDto requestDto) {

        if (!requestDto.isDayOfWeekRepeat()) return Collections.emptyList();

        if (requestDto.dayOfWeeks() == null) throw new BusinessException(BAD_REQUEST);

        return requestDto.dayOfWeeks();
    }
    
    private void createTimeEventSchedulesForEmployee(CreateTimeEventsRequestDto requestDto, List<DayOfWeek> dayOfWeeks, TimeEvent timeEvent) {

        if (requestDto.isDayOfWeekRepeat())
            processTimeEventScheduleByDayOfWeeks(requestDto, dayOfWeeks, timeEvent);

        if (requestDto.isDateRepeat())
            validateDateIsNull(requestDto);
            processTimeEventScheduleByDateRepeat(requestDto, timeEvent);

        if (!requestDto.isRepeat())
            processTimeEventScheduleToday(requestDto, timeEvent);
    }

    private void validateDateIsNull(CreateTimeEventsRequestDto requestDto) {
        if (requestDto.startDate() == null || requestDto.endDate() == null) {
            throw new BusinessException(BAD_REQUEST);
        }
    }

    /**
     * reservationSchedules에 newTimeEventSchedule을 연결합니다.
     *
     * @param newTimeEventSchedule 새로운 타임이벤트 일정
     * @param reservationSchedulesWithinEventPeriod 생성한 타임이벤트 일정과 시간이 겹치는 예약 일정들
     */
    private void linkEventScheduleAndReservationSchedule(EmployTimeEventSchedule newTimeEventSchedule, List<ReservationSchedule> reservationSchedulesWithinEventPeriod) {
        reservationSchedulesWithinEventPeriod.stream().forEach(reservationSchedule -> {
            reservationSchedule.addEmployeeTimeEventSchedule(newTimeEventSchedule);
        });
    }

    /**
     * 새로운 타임 이벤트 일정이 기존 타임 이벤트 일정과 겹치는지 검사합니다.
     *
     * @param newOpenEventDateTime 새로운 타임 이벤트 시작 날짜
     * @param newEndEventDateTime 새로운 타임 이벤트 마감 날짜
     * @param employee 타임 이벤트 일정이 추가될 직원
     */
    private void validateOverlapEventSchedule(LocalDateTime newOpenEventDateTime, LocalDateTime newEndEventDateTime, Employee employee) {
        boolean isOverlapped = employTimeEventScheduleRepository.existsOverlappingEventScheduleWithinEventPeriod(newOpenEventDateTime, newEndEventDateTime, employee);
        if (isOverlapped)
            throw new ConflictException(ErrorCode.EXIST_EVENTS_CONFLICT);

        }

    private EmployTimeEventSchedule createNewEmployTimeEventSchedule(LocalDateTime newStartEventDateTime, LocalDateTime newEndEventDateTime, Employee employee) {
        validateOverlapEventSchedule(newStartEventDateTime, newEndEventDateTime, employee);

        EmployTimeEventSchedule newEmployTimeEventSchedule = EmployTimeEventSchedule.builder()
                .startEventDateTime(newStartEventDateTime)
                .endEventDateTime(newEndEventDateTime)
                .build();
        newEmployTimeEventSchedule.addEmployee(employee);

        return newEmployTimeEventSchedule;
    }

    private EmployTimeEventSchedule createAndSaveTimeEventSchedule(LocalDateTime newStartEventDateTime, LocalDateTime newEndEventDateTime, Employee employee, TimeEvent timeEvent) {
        EmployTimeEventSchedule newTimeEventSchedule = createNewEmployTimeEventSchedule(newStartEventDateTime, newEndEventDateTime, employee);
        newTimeEventSchedule.addTimeEvent(timeEvent);
        employTimeEventScheduleRepository.save(newTimeEventSchedule);
        return newTimeEventSchedule;
    }

    /**
     *
     * @param newStartEventDateTime 새로운 타임이벤트 시작날짜
     * @param newEndEventDateTime 새로운 타임이벤트 마감날짜
     * @param employee 직원
     * @param reservationSchedulesWithinEventPeriod 새로운 타임이벤트 일정에 포함된 직원의 예약 일정들
     */
    private void processTimeEventSchedule(LocalDateTime newStartEventDateTime, LocalDateTime newEndEventDateTime, Employee employee,
                                          List<ReservationSchedule> reservationSchedulesWithinEventPeriod, TimeEvent timeEvent) {
        EmployTimeEventSchedule newTimeEventSchedule = createAndSaveTimeEventSchedule(newStartEventDateTime, newEndEventDateTime, employee, timeEvent);
        linkEventScheduleAndReservationSchedule(newTimeEventSchedule, reservationSchedulesWithinEventPeriod);
    }

    private List<Employee> getEmployees(CreateTimeEventsRequestDto requestDto) {
        List<Employee> employees = requestDto.employeeIds().stream().map(employId ->
                employeeRepository.findById(employId).orElseThrow(() -> new EntityNotFoundException(EMPLOYEE_NOT_FOUND))).collect(Collectors.toList());
        return employees;
    }

    /**
     * 요일 반복 요청에 따른 TimeEventSchedule 처리
     *
     * 예약 일정이 생성되어 있으며, 반복 요청된 요일에 해당하는 경우의 날짜에 타임이벤트 일정을 생성합니다.
     * 직원의 예약 일정이 생성된 타임이벤트 일정에 포함되어 있으면 예약 일정에 타임이벤트 일정을 추가합니다.
     *
     * @param requestDto
     * @param newDayOfWeeks 반복 요청된 요일 ex. [MONDAY,TUESDAY ...]
     */
    private void processTimeEventScheduleByDayOfWeeks(CreateTimeEventsRequestDto requestDto, List<DayOfWeek> newDayOfWeeks,TimeEvent timeEvent) {
        List<Employee> employees = getEmployees(requestDto);

        for (Employee em : employees) {
            List<ReservationSchedule> reservationSchedules = reservationScheduleRepository
                    .findReservationSchedulesByEmployeeAndDateAfter(em, LocalDate.now().minusDays(1));
            Map<LocalDate, List<ReservationSchedule>> reservationSchedulesByDate = groupReservationSchedulesByDate(reservationSchedules);

            reservationSchedulesByDate.forEach((date, schedules) -> {
                if (isSameDayOfWeek(newDayOfWeeks, date)) {
                    LocalDateTime newStartEventDateTime = LocalDateTime.of(date, requestDto.startTime());
                    LocalDateTime newEndEventDateTime = LocalDateTime.of(date, requestDto.endTime());

                    processTimeEventSchedule(newStartEventDateTime, newEndEventDateTime, em,
                            getFilteredReservationSchedulesForDay(requestDto, schedules),timeEvent);
                }
            });
        }
    }
    
    private Map<LocalDate, List<ReservationSchedule>> groupReservationSchedulesByDate(List<ReservationSchedule> reservationSchedules) {
        return reservationSchedules.stream()
                .collect(Collectors.groupingBy(ReservationSchedule::getWorkDate, HashMap::new, Collectors.toList()));
    }

    private boolean isSameDayOfWeek(List<DayOfWeek> newDayOfWeeks, LocalDate date) {
        return newDayOfWeeks.contains(date.getDayOfWeek());
    }

    /**
     * 하루 ( ex.2024년 5월 13일 )의 여러 예약 일정들 중
     * 새로운 타임이벤트의 시작 시간 ~ 마감시간과 겹치는 일정을 가지는 예약 일정들을 필터링하여 반환합니다.
     *
     * @param requestDto
     * @param schedules 특정 직원의 예약 일정들
     * @return 생성될 타임이벤트와 일정이 겹치는 예약 일정들
     */
    private List<ReservationSchedule> getFilteredReservationSchedulesForDay(CreateTimeEventsRequestDto requestDto, List<ReservationSchedule> schedules) {
        List<ReservationSchedule> reservationSchedules = schedules.stream()
                .filter(schedule ->
                        schedule.getEndTime().isAfter(requestDto.startTime()) && schedule.getStartTime().isBefore(requestDto.endTime()))
                .collect(Collectors.toList());
        return reservationSchedules;
    }

    private List<ReservationSchedule> filterReservationSchedule(LocalDate eventStartDate, LocalTime eventStartTime, LocalDate eventEndDate, LocalTime eventEndTime , List<ReservationSchedule> reservationSchedulesWithinEventPeriod) {

        List<ReservationSchedule> reservationSchedules = reservationSchedulesWithinEventPeriod.stream().filter(reservationSchedule ->{
            boolean isStartDateEqual = reservationSchedule.getWorkDate().isEqual(eventStartDate);
            boolean isEndDateEqual = reservationSchedule.getWorkDate().isEqual(eventEndDate);
            boolean isBeforeStartTime = isStartDateEqual && reservationSchedule.getStartTime().isBefore(eventStartTime);
            boolean isAfterEndTime = isEndDateEqual && reservationSchedule.getEndTime().isAfter(eventEndTime);

            // 이 조건은 시작 날짜가 동일하고 시작 시간 이전에 시작하는 스케줄을 제외하거나,
            // 끝나는 날짜가 동일하고 종료 시간 이후에 끝나는 스케줄을 제외합니다.
            return !(isBeforeStartTime || isAfterEndTime);
        }).collect(Collectors.toList());

        return reservationSchedules;
    }

    private List<ReservationSchedule> findReservationScheduleWithinEventPeriod(LocalDateTime newStartEventDateTime, LocalDateTime newEndEventDateTime, Employee em) {
        LocalDate eventStartDate = LocalDate.from(newStartEventDateTime);
        LocalDate eventEndDate = LocalDate.from(newEndEventDateTime);
        LocalTime eventStartTime = LocalTime.from(newStartEventDateTime);
        LocalTime eventEndTime = LocalTime.from(newEndEventDateTime);

        List<ReservationSchedule> reservationSchedulesWithinEventPeriod =
                reservationScheduleRepository.findOverlappingReservationSchedulesWithinEventPeriod(eventStartDate, eventEndDate, em);

        return filterReservationSchedule(eventStartDate,eventStartTime, eventEndDate,eventEndTime, reservationSchedulesWithinEventPeriod);

    }

    /**
     * 기간 반복 요청에 따른 TimeEventSchedule 처리
     *
     * requestDto의 startDate, startTime을 타임이벤트 시작 날짜,
     * endDate, endTime을 타임이벤트 마감 날짜로 설정하여 타임이벤트스케쥴을 생성합니다.
     * 직원의 예약 일정이 생성된 타임이벤트 일정에 포함되어 있으면 예약 일정에 타임이벤트 일정을 추가합니다.
     *
     * @param requestDto
     */
    private void processTimeEventScheduleByDateRepeat(CreateTimeEventsRequestDto requestDto, TimeEvent timeEvent) {
        LocalDateTime newStartEventDateTime = LocalDateTime.of(requestDto.startDate(), requestDto.startTime());
        LocalDateTime newEndEventDateTime = LocalDateTime.of(requestDto.endDate(), requestDto.endTime());

        getEmployees(requestDto).stream()
                .forEach(employee -> {
                    processTimeEventSchedule(newStartEventDateTime, newEndEventDateTime, employee,
                            findReservationScheduleWithinEventPeriod(newStartEventDateTime, newEndEventDateTime, employee),timeEvent);
                });
    }

    /**
     * 반복 요청을 하지 않은 경우의 TimeEventSchedule 처리
     *
     * 이벤트 생성 요청을 한 날짜와, requestDto의 startTime, endTime을 기반으로 새로운 타임이벤트 일정을 생성합니다.
     * 직원의 예약 일정이 생성된 타임이벤트 일정에 포함되어 있으면 예약 일정에 타임이벤트 일정을 추가합니다.
     *
     * @param requestDto
     */
    private void processTimeEventScheduleToday(CreateTimeEventsRequestDto requestDto, TimeEvent timeEvent) {
        LocalDateTime newStartEventDateTime = LocalDateTime.of(LocalDate.now(), requestDto.startTime());
        LocalDateTime newEndEventDateTime = LocalDateTime.of(LocalDate.now(), requestDto.endTime());

        getEmployees(requestDto).stream()
                .forEach(employee -> {
                    processTimeEventSchedule(newStartEventDateTime, newEndEventDateTime, employee,
                            findReservationScheduleWithinEventPeriod(newStartEventDateTime, newEndEventDateTime, employee), timeEvent);
                });
    }



    private TimeEvent createTimeEvent(CreateTimeEventsRequestDto requestDto, Shop shop, List<DayOfWeek> dayOfWeeks) {

        TimeEvent timeEvent = TimeEvent.builder()
                .title(requestDto.title())
                .discountRate(requestDto.discountRate())
                .dayOfWeeks(dayOfWeeks)
                .repetitionStatus(requestDto.isRepeat())
                .isDateRepeat(requestDto.isDateRepeat())
                .isDayOfWeekRepeat(requestDto.isDayOfWeekRepeat())
                .startTime(requestDto.startTime())
                .endTime(requestDto.endTime())
                .shop(shop)
                .build();

        return timeEvent;
    }

    private void setMenuAndTimeEventToTimeEventMenu(CreateTimeEventsRequestDto requestDto, TimeEvent timeEvent) {
        requestDto.menus().stream()
                .map(id -> menuRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(MENU_NOT_FOUND)))
                .forEach(menu -> {
                    TimeEventMenu timeEventMenu = TimeEventMenu.builder()
                            .menu(menu)
                            .timeEvent(timeEvent).build();
                    timeEventMenu.addMenu(menu);
                    timeEventMenu.addTimeEvent(timeEvent);
                });
    }

    private void setEmployeeAndTimeEventToEmployeeTimeEvent(CreateTimeEventsRequestDto requestDto, TimeEvent timeEvent) {
        requestDto.employeeIds().stream()
                .map(id -> employeeRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(EMPLOYEE_NOT_FOUND)))
                .forEach(employee -> {
                    EmployeeTimeEvent employeeTimeEvent = EmployeeTimeEvent.builder()
                            .employee(employee)
                            .timeEvent(timeEvent).build();
                    employeeTimeEvent.addTimeEvent(timeEvent);
                    employeeTimeEvent.addEmployee(employee);
                });
    }

}
