package kyonggi.bookslyserver.domain.event.service;

import kyonggi.bookslyserver.domain.event.dto.response.GetAvailableDatesResponseDto;
import kyonggi.bookslyserver.domain.event.dto.response.GetTimeEventsResponseDto;
import kyonggi.bookslyserver.domain.event.dto.response.GetTodayClosingEventsResponseDto;
import kyonggi.bookslyserver.domain.event.dto.response.GetTodayTimeEventsResponseDto;
import kyonggi.bookslyserver.domain.event.entity.timeEvent.TimeEvent;
import kyonggi.bookslyserver.domain.event.repository.TimeEventRepository;
import kyonggi.bookslyserver.domain.event.repository.TimeEventScheduleRepository;
import kyonggi.bookslyserver.domain.reservation.entity.ReservationSchedule;
import kyonggi.bookslyserver.domain.reservation.entity.ReservationSetting;
import kyonggi.bookslyserver.domain.reservation.repository.ReservationScheduleRepository;
import kyonggi.bookslyserver.domain.reservation.repository.ReservationSettingRepository;
import kyonggi.bookslyserver.domain.reservation.service.ReserveQueryService;
import kyonggi.bookslyserver.domain.shop.entity.BusinessSchedule.BusinessSchedule;
import kyonggi.bookslyserver.domain.shop.entity.BusinessSchedule.DayName;
import kyonggi.bookslyserver.domain.shop.entity.Employee.Employee;
import kyonggi.bookslyserver.domain.shop.entity.Shop.Shop;
import kyonggi.bookslyserver.domain.shop.repository.EmployeeRepository;
import kyonggi.bookslyserver.domain.shop.service.ShopService;
import kyonggi.bookslyserver.global.error.exception.EntityNotFoundException;
import kyonggi.bookslyserver.global.util.TimeUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static kyonggi.bookslyserver.global.error.ErrorCode.EMPLOYEE_NOT_FOUND;
import static kyonggi.bookslyserver.global.error.ErrorCode.SETTING_NOT_FOUND;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class TimeEventQueryService {

    private final ShopService shopService;
    private final EmployeeRepository employeeRepository;
    private final TimeEventScheduleRepository timeEventScheduleRepository;
    private final ReservationSettingRepository reservationSettingRepository;
    private final TimeEventRepository timeEventRepository;
    private final ReserveQueryService reserveQueryService;
    private final ReservationScheduleRepository reservationScheduleRepository;

    public GetTimeEventsResponseDto getTimeEvents(Long shopId, Long employeeId, LocalDate date, Long ownerId) {
        Shop shop = shopService.findShop(shopId, ownerId);
        Employee employee = employeeRepository.findByIdAndShopId(employeeId, shop.getId())
                .orElseThrow(() -> new EntityNotFoundException(EMPLOYEE_NOT_FOUND));

        if (date == null) date = LocalDate.now();

        LocalDateTime dateTimeStart = date.atStartOfDay();
        LocalDateTime dateTimeEnd = date.atTime(LocalTime.MAX);

        Optional<List<TimeEvent>> timeEvents = timeEventScheduleRepository.findTimeEventsByEmployeeIdAndDateTime(employee.getId(), dateTimeStart, dateTimeEnd);
        return GetTimeEventsResponseDto.of(timeEvents);
    }

    public GetAvailableDatesResponseDto getAvailableDates(Long shopId, Long ownerId) {
        Shop shop = shopService.findShop(shopId, ownerId);
        ReservationSetting reservationSetting = reservationSettingRepository.findByShop(shop).orElseThrow(() -> new EntityNotFoundException(SETTING_NOT_FOUND));
        int reservationCycle = reservationSetting.getReservationCycle();

        LocalDate currentDate = LocalDate.now();
        LocalDate plusDate = currentDate.plusDays(reservationCycle-1);

        List<BusinessSchedule> businessSchedules = shop.getBusinessSchedules();
        Map<DayName,Boolean> workInfo = businessSchedules.stream()
                .filter(schedule -> schedule != null && schedule.getDay() != null)
                .collect(Collectors.toMap(BusinessSchedule::getDay,BusinessSchedule::getIsHoliday));

        List<LocalDate> workdays = new ArrayList<>();

        while (!currentDate.isAfter(plusDate)) {
            DayName dayName = convertDayOfWeekToDayName(currentDate.getDayOfWeek());
            Boolean isDayOff = workInfo.get(dayName);
            if (!isDayOff) workdays.add(currentDate);

            currentDate = currentDate.plusDays(1);
        }

        return GetAvailableDatesResponseDto.of(workdays);
    }

    public DayName convertDayOfWeekToDayName(DayOfWeek dayOfWeek) {
        switch (dayOfWeek) {
            case MONDAY:
                return DayName.MONDAY;
            case TUESDAY:
                return DayName.TUESDAY;
            case WEDNESDAY:
                return DayName.WEDNESDAY;
            case THURSDAY:
                return DayName.THURSDAY;
            case FRIDAY:
                return DayName.FRIDAY;
            case SATURDAY:
                return DayName.SATURDAY;
            case SUNDAY:
                return DayName.SUNDAY;
            default:
                throw new IllegalArgumentException("Unknown DayOfWeek: " + dayOfWeek);
        }
    }

    public GetTodayTimeEventsResponseDto getTodayTimeEvents(List<String> regions, List<String> timeSlots, List<Long> categories) {

        List<Shop> shops = timeEventRepository.findShopsByTimeEvent();

        //지역 필터링 조건 적용
        if (regions != null && !regions.isEmpty()) shops = shopService.getRegionFilteredShops(regions, shops);

        //카테고리 필터링 조건 적용
        if (categories != null && !categories.isEmpty()) shops = shopService.getCategoryFilteredShops(categories, shops);

        if (timeSlots == null || timeSlots.isEmpty()){
            String timeSlot = LocalTime.now() + "-" + LocalTime.of(23, 59, 59);
            timeSlots = new ArrayList<>();
            timeSlots.add(timeSlot);
        }
        List<ReservationSchedule> timeSlotFilteredEventSchedules = getTimeSlotFilteredEventSchedules(timeSlots, shops);

        return GetTodayTimeEventsResponseDto.of(reserveQueryService.sortSchedulesByStartTimeThenRating(timeSlotFilteredEventSchedules));

    }

    private List<ReservationSchedule> getTimeSlotFilteredEventSchedules(List<String> timeSlots, List<Shop> shops) {
        List<ReservationSchedule> earliestEventSchedulesForTimeSlot = new ArrayList<>();
        Pageable firstResult = PageRequest.of(0, 1);
        LocalDate nowDate = LocalDate.now();

        timeSlots.forEach(timeSlot -> {
            LocalTime[] times = TimeUtil.parseTimeSlot(timeSlot);
            LocalTime startTime = times[0];
            LocalTime endTime = times[1];

            shops.stream()
                    .map(shop -> reservationScheduleRepository.findTimeEventSchedules(nowDate, startTime, endTime, shop, firstResult))
                    .filter(reservationSchedules -> !reservationSchedules.isEmpty())
                    .forEach(reservationSchedules -> earliestEventSchedulesForTimeSlot.add(reservationSchedules.get(0)));
        });

        return earliestEventSchedulesForTimeSlot;
    }
}
