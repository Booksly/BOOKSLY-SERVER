package kyonggi.bookslyserver.domain.event.service;

import kyonggi.bookslyserver.domain.event.dto.request.CreateLocalTimeEventsRequestDto;
import kyonggi.bookslyserver.domain.event.dto.response.CreateTimeEventsResponseDto;
import kyonggi.bookslyserver.domain.event.dto.response.DeleteTimeEventResponseDto;
import kyonggi.bookslyserver.domain.event.entity.timeEvent.*;
import kyonggi.bookslyserver.domain.event.repository.TimeEventRepository;
import kyonggi.bookslyserver.domain.event.repository.TimeEventScheduleRepository;
import kyonggi.bookslyserver.domain.reservation.entity.ReservationSchedule;
import kyonggi.bookslyserver.domain.reservation.repository.ReservationScheduleRepository;
import kyonggi.bookslyserver.domain.shop.entity.Employee.Employee;
import kyonggi.bookslyserver.domain.shop.entity.Menu.Menu;
import kyonggi.bookslyserver.domain.shop.entity.Shop.Shop;
import kyonggi.bookslyserver.domain.shop.repository.EmployeeMenuRepository;
import kyonggi.bookslyserver.domain.shop.repository.EmployeeRepository;
import kyonggi.bookslyserver.domain.shop.repository.MenuRepository;
import kyonggi.bookslyserver.domain.shop.service.ShopService;
import kyonggi.bookslyserver.global.error.ErrorCode;
import kyonggi.bookslyserver.global.error.exception.*;
import kyonggi.bookslyserver.global.util.TimeUtil;
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
    private final EmployeeMenuRepository employeeMenuRepository;
    private final MenuRepository menuRepository;
    private final TimeEventRepository timeEventRepository;
    private final TimeEventScheduleRepository timeEventScheduleRepository;
    private final ReservationScheduleRepository reservationScheduleRepository;
    private final ShopService shopService;

    public CreateTimeEventsResponseDto createTimeEvents(Long ownerId, CreateLocalTimeEventsRequestDto requestDto) {

        validateRepeatSettings(requestDto);
        validateEventTiming(requestDto);

        List<Employee> employees = getEmployees(requestDto.employeeIds());
        validateEmployeesBelongToShop(requestDto, employees);
        validateEmployeeMenus(requestDto.menus(), employees);

        Shop shop = shopService.findShop(ownerId, requestDto.shopId());
        List<DayOfWeek> dayOfWeeks = createRepeatDayOfWeeks(requestDto);
        TimeEvent timeEvent = createTimeEvent(requestDto, shop, dayOfWeeks);
        timeEventRepository.save(timeEvent);

        createTimeEventSchedulesForEmployee(requestDto,dayOfWeeks,timeEvent,employees);

        setMenuAndTimeEventToTimeEventMenu(requestDto, timeEvent);
        setEmployeeAndTimeEventToEmployeeTimeEvent(requestDto, timeEvent);

        return CreateTimeEventsResponseDto.of(timeEvent);
    }

    private void validateRepeatSettings(CreateLocalTimeEventsRequestDto requestDto) {
        boolean isRepeat = requestDto.isRepeat();
        boolean isDateRepeat = requestDto.isDateRepeat();
        boolean isDayOfWeekRepeat = requestDto.isDayOfWeekRepeat();

        if (isRepeat && isDateRepeat && isDayOfWeekRepeat) {
            throw new InvalidValueException(DUPLICATED_REPEAT_SETTING_BAD_REQUEST);
        }
        if (isRepeat && !isDateRepeat && !isDayOfWeekRepeat) {
            throw new InvalidValueException(INCOMPLETE_REPEAT_SETTING_BAD_REQUEST);
        }
        if (!isRepeat && (isDateRepeat || isDayOfWeekRepeat)) {
            throw new InvalidValueException(BAD_REQUEST);
        }
    }

    private void validateEventTiming(CreateLocalTimeEventsRequestDto requestDto) {
        if (requestDto.isDateRepeat()) {
            validateDate(requestDto.startDate(), requestDto.endDate());
        } else {
            validateTimeOrder(requestDto.startTime(), requestDto.endTime());
        }
        if (requestDto.isRepeat()) {
            validateStartTimeIsAfterNow(requestDto.startTime());
        }
    }

    private void validateDate(LocalDate startDate, LocalDate endDate) {
        if (!TimeUtil.checkDateOrder(startDate, endDate)) {
            throw new InvalidValueException(START_DATE_IS_AFTER_END_DATE);
        }
        if (!TimeUtil.isDateAfterNow(startDate)) {
            throw new InvalidValueException(START_DATE_IS_BEFORE_NOW_DATE);
        }
    }

    private void validateTimeOrder(LocalTime startTime, LocalTime endTime) {
        if (!TimeUtil.checkTimeOrder(startTime, endTime)) {
            throw new InvalidValueException(START_TIME_IS_AFTER_END_TIME);
        }
    }

    private void validateStartTimeIsAfterNow(LocalTime startTime) {
        if (!TimeUtil.isTimeAfterNow(startTime)) {
            throw new InvalidValueException(STRAT_TIME_IS_BEFORE_NOW);
        }
    }

    private void validateEmployeesBelongToShop(CreateLocalTimeEventsRequestDto requestDto, List<Employee> employees) {
        if (employeeRepository.countByEmployeesAndShop(employees, requestDto.shopId()) != requestDto.employeeIds().size())
            throw new InvalidValueException(EMPLOYEE_NOT_BELONG_SHOP);
    }

    private void validateEmployeeMenus(List<Long> menuIds, List<Employee> employees) {
        // 등록 요청 메뉴를 직원 누구도 담당하고 있지 않을 때 에러 반환
        List<Menu> menus = employeeMenuRepository.findMenusByMenuIdsAndEmployees(menuIds, employees);
        if (menus.size() != menuIds.size()) {
            throw new InvalidValueException(MENU_IS_NOT_EMPLOYEEMENU);
        }
    }

    private List<DayOfWeek> createRepeatDayOfWeeks(CreateLocalTimeEventsRequestDto requestDto) {

        if (!requestDto.isDayOfWeekRepeat()) return Collections.emptyList();

        if (requestDto.dayOfWeeks() == null) throw new BusinessException(BAD_REQUEST);

        return requestDto.dayOfWeeks();
    }
    
    private void createTimeEventSchedulesForEmployee(CreateLocalTimeEventsRequestDto requestDto, List<DayOfWeek> dayOfWeeks, TimeEvent timeEvent, List<Employee> employees) {

        if (requestDto.isDayOfWeekRepeat())
            processTimeEventScheduleByDayOfWeeks(requestDto, dayOfWeeks, timeEvent, employees);

        if (requestDto.isDateRepeat()){
            processTimeEventScheduleByDateRepeat(requestDto, timeEvent, employees);}

        if (!requestDto.isRepeat())
            processTimeEventScheduleToday(requestDto, timeEvent, employees);
    }

    /**
     * reservationSchedules에 newTimeEventSchedule을 연결합니다.
     *
     * @param newTimeEventSchedule 새로운 타임이벤트 일정
     * @param reservationSchedulesWithinEventPeriod 생성한 타임이벤트 일정과 시간이 겹치는 예약 일정들
     */
    private void linkEventScheduleAndReservationSchedule(TimeEventSchedule newTimeEventSchedule, List<ReservationSchedule> reservationSchedulesWithinEventPeriod) {
        reservationSchedulesWithinEventPeriod.stream().forEach(reservationSchedule -> {
            reservationSchedule.addTimeEventSchedule(newTimeEventSchedule);
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
        boolean isOverlapped = timeEventScheduleRepository.existsOverlappingEventScheduleWithinEventPeriod(newOpenEventDateTime, newEndEventDateTime, employee);
        if (isOverlapped)
            throw new ConflictException(ErrorCode.EXIST_EVENTS_CONFLICT);

        }

    private TimeEventSchedule createNewTimeEventSchedule(LocalDateTime newStartEventDateTime, LocalDateTime newEndEventDateTime, Employee employee) {
        validateOverlapEventSchedule(newStartEventDateTime, newEndEventDateTime, employee);

        TimeEventSchedule newTimeEventSchedule = TimeEventSchedule.builder()
                .startEventDateTime(newStartEventDateTime)
                .endEventDateTime(newEndEventDateTime)
                .build();
        newTimeEventSchedule.addEmployee(employee);

        return newTimeEventSchedule;
    }

    private TimeEventSchedule createAndSaveTimeEventSchedule(LocalDateTime newStartEventDateTime, LocalDateTime newEndEventDateTime, Employee employee, TimeEvent timeEvent) {
        TimeEventSchedule newTimeEventSchedule = createNewTimeEventSchedule(newStartEventDateTime, newEndEventDateTime, employee);
        newTimeEventSchedule.addTimeEvent(timeEvent);
        timeEventScheduleRepository.save(newTimeEventSchedule);
        return newTimeEventSchedule;
    }

    /**
     *
     * @param newStartEventDateTime 새로운 타임이벤트 시작날짜
     * @param newEndEventDateTime 새로운 타임이벤트 마감날짜
     * @param employee 직원
     * @param reservationSchedulesWithinEventPeriod 새로운 타임이벤트 일정에 포함된 직원의 예약 일정들
     */
    private void createTimeEventSchedule(LocalDateTime newStartEventDateTime, LocalDateTime newEndEventDateTime, Employee employee,
                                         List<ReservationSchedule> reservationSchedulesWithinEventPeriod, TimeEvent timeEvent) {
        TimeEventSchedule newTimeEventSchedule = createAndSaveTimeEventSchedule(newStartEventDateTime, newEndEventDateTime, employee, timeEvent);
        linkEventScheduleAndReservationSchedule(newTimeEventSchedule, reservationSchedulesWithinEventPeriod);
    }

    /**
     * filteredReservationSchedule가 0인 경우 예외 반환
     * @param filteredReservationSchedule 이벤트 생성 요청 시간 범위에 포함되는 예약 일정들
     */
    private void validateScheduleSize(List<ReservationSchedule> filteredReservationSchedule) {
        if (filteredReservationSchedule.size() == 0) {
            throw new InvalidValueException(SCHEDULE_NOT_INCLUDED);
        }
    }

    private List<Employee> getEmployees(List<Long> employeeIds) {
        List<Employee> employees = employeeIds.stream().map(employId ->
                employeeRepository.findById(employId).orElseThrow(() -> new EntityNotFoundException(EMPLOYEE_NOT_FOUND))).collect(Collectors.toList());
        return employees;
    }

    /**
     * 요일 반복 요청에 따른 TimeEventSchedule 생성
     * <p>
     * 1. 직원이 가지고 있는 여러 예약 일정 중, 타임이벤트 생성을 요청한 요일에 해당하는 날짜인 경우를 찾습니다.
     * 2. startTime ~ endTime 에 따라 TimeEventSchedule을 생성하고 이를 해당 예약 일정에 추가합니다.
     *
     * @param requestDto
     * @param newDayOfWeeks 반복 요청된 요일 ex. [MONDAY,TUESDAY ...]
     * @param employees
     */
    private void processTimeEventScheduleByDayOfWeeks(CreateLocalTimeEventsRequestDto requestDto, List<DayOfWeek> newDayOfWeeks, TimeEvent timeEvent, List<Employee> employees) {

        for (Employee em : employees) {
            List<ReservationSchedule> reservationSchedules = reservationScheduleRepository
                    .findReservationSchedulesByEmployeeAndDateAfter(em, LocalDate.now().minusDays(1));
            Map<LocalDate, List<ReservationSchedule>> reservationSchedulesByDate = groupReservationSchedulesByDate(reservationSchedules);

            reservationSchedulesByDate.forEach((date, schedules) -> {
                if (isSameDayOfWeek(newDayOfWeeks, date)) {
                    LocalDateTime newStartEventDateTime = LocalDateTime.of(date, requestDto.startTime());
                    LocalDateTime newEndEventDateTime = LocalDateTime.of(date, requestDto.endTime());

                    List<ReservationSchedule> filteredReservationSchedulesForDay = getFilteredReservationSchedulesForDay(requestDto, schedules);
                    validateScheduleSize(filteredReservationSchedulesForDay);

                    createTimeEventSchedule(newStartEventDateTime, newEndEventDateTime, em, filteredReservationSchedulesForDay,timeEvent);
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
    private List<ReservationSchedule> getFilteredReservationSchedulesForDay(CreateLocalTimeEventsRequestDto requestDto, List<ReservationSchedule> schedules) {
        List<ReservationSchedule> reservationSchedules = schedules.stream()
                .filter(schedule ->
                        schedule.getEndTime().isAfter(requestDto.startTime()) && schedule.getStartTime().isBefore(requestDto.endTime()))
                .collect(Collectors.toList());
        return reservationSchedules;
    }

    private List<ReservationSchedule> filterReservationSchedule(LocalDate eventStartDate, LocalTime eventStartTime, LocalDate eventEndDate, LocalTime eventEndTime , List<ReservationSchedule> reservationSchedulesWithinEventPeriod) {

        List<ReservationSchedule> filteredReservationSchedule = reservationSchedulesWithinEventPeriod.stream().filter(reservationSchedule ->{
            boolean isStartDateEqual = reservationSchedule.getWorkDate().isEqual(eventStartDate);
            boolean isEndDateEqual = reservationSchedule.getWorkDate().isEqual(eventEndDate);
            boolean isBeforeStartTime = isStartDateEqual && reservationSchedule.getStartTime().isBefore(eventStartTime);
            boolean isAfterEndTime = isEndDateEqual && reservationSchedule.getEndTime().isAfter(eventEndTime);

            // 이 조건은 시작 날짜가 동일하고 시작 시간 이전에 시작하는 스케줄을 제외하거나,
            // 끝나는 날짜가 동일하고 종료 시간 이후에 끝나는 스케줄을 제외합니다.
            return !(isBeforeStartTime || isAfterEndTime);
        }).collect(Collectors.toList());

        validateScheduleSize(filteredReservationSchedule);

        return filteredReservationSchedule;
    }


    private List<ReservationSchedule> findReservationScheduleWithinEventPeriod(LocalDateTime newStartEventDateTime, LocalDateTime newEndEventDateTime, Employee em) {
        LocalDate eventStartDate = LocalDate.from(newStartEventDateTime);
        LocalDate eventEndDate = LocalDate.from(newEndEventDateTime);
        LocalTime eventStartTime = LocalTime.from(newStartEventDateTime);
        LocalTime eventEndTime = LocalTime.from(newEndEventDateTime);

        List<ReservationSchedule> reservationSchedulesWithinEventPeriod =
                reservationScheduleRepository.findOverlappingReservationSchedulesWithinEventPeriod(eventStartDate, eventEndDate, em);

        List<ReservationSchedule> filteredReservationSchedule = filterReservationSchedule(eventStartDate, eventStartTime, eventEndDate, eventEndTime, reservationSchedulesWithinEventPeriod);
        return filteredReservationSchedule;

    }

    private void processTimeEventSchedule(TimeEvent timeEvent, LocalDateTime newStartEventDateTime, LocalDateTime newEndEventDateTime, List<Employee> employees) {
        employees.forEach(employee -> {
                    List<ReservationSchedule> filteredReservationSchedules = findReservationScheduleWithinEventPeriod(newStartEventDateTime, newEndEventDateTime, employee);
                    validateScheduleSize(filteredReservationSchedules);

                    createTimeEventSchedule(newStartEventDateTime, newEndEventDateTime, employee, filteredReservationSchedules, timeEvent);
                });
    }

    /**
     * 기간 반복 요청에 따른 TimeEventSchedule 처리
     * <p>
     * requestDto의 startDate, startTime을 타임이벤트 시작 날짜,
     * endDate, endTime을 타임이벤트 마감 날짜로 설정하여 타임이벤트스케쥴을 생성합니다.
     * 직원의 예약 일정이 생성된 타임이벤트 일정에 포함되어 있으면 예약 일정에 타임이벤트 일정을 추가합니다.
     *
     * @param requestDto
     * @param employees
     */
    private void processTimeEventScheduleByDateRepeat(CreateLocalTimeEventsRequestDto requestDto, TimeEvent timeEvent, List<Employee> employees) {
        LocalDateTime newStartEventDateTime = LocalDateTime.of(requestDto.startDate(), requestDto.startTime());
        LocalDateTime newEndEventDateTime = LocalDateTime.of(requestDto.endDate(), requestDto.endTime());

        processTimeEventSchedule(timeEvent, newStartEventDateTime, newEndEventDateTime, employees);
    }

    /**
     * 반복 요청을 하지 않은 경우의 TimeEventSchedule 처리
     * <p>
     * 이벤트 생성 요청을 한 날짜와, requestDto의 startTime, endTime을 기반으로 새로운 타임이벤트 일정을 생성합니다.
     * 직원의 예약 일정이 생성된 타임이벤트 일정에 포함되어 있으면 예약 일정에 타임이벤트 일정을 추가합니다.
     *
     * @param requestDto
     * @param employees
     */
    private void processTimeEventScheduleToday(CreateLocalTimeEventsRequestDto requestDto, TimeEvent timeEvent, List<Employee> employees) {
        LocalDateTime newStartEventDateTime = LocalDateTime.of(LocalDate.now(), requestDto.startTime());
        LocalDateTime newEndEventDateTime = LocalDateTime.of(LocalDate.now(), requestDto.endTime());

        processTimeEventSchedule(timeEvent, newStartEventDateTime, newEndEventDateTime, employees);
    }


    private TimeEvent createTimeEvent(CreateLocalTimeEventsRequestDto requestDto, Shop shop, List<DayOfWeek> dayOfWeeks) {

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

    private void setMenuAndTimeEventToTimeEventMenu(CreateLocalTimeEventsRequestDto requestDto, TimeEvent timeEvent) {
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

    private void setEmployeeAndTimeEventToEmployeeTimeEvent(CreateLocalTimeEventsRequestDto requestDto, TimeEvent timeEvent) {
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

    public DeleteTimeEventResponseDto deleteEvent(Long shopId, Long eventId, Long ownerId) {
        Shop shop = shopService.findShop(ownerId, shopId);
        TimeEvent timeEvent = timeEventRepository.findById(eventId).orElseThrow(() -> new EntityNotFoundException(TIME_EVENT_NOT_FOUND));

        //가게 소유 타임이벤트가 아니면 불가
        if (!shop.getId().equals(timeEvent.getShop().getId())) {
            throw new ForbiddenException();}

        List<ReservationSchedule> reservationSchedules = reservationScheduleRepository.findByTimeEventSchedules(timeEvent.getTimeEventSchedules());
        reservationSchedules.stream().forEach(reservationSchedule -> reservationSchedule.cancelTimeEvent());

        timeEventRepository.delete(timeEvent);
        return DeleteTimeEventResponseDto.of(eventId);
    }
}
