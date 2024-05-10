package kyonggi.bookslyserver.domain.event.service;

import kyonggi.bookslyserver.domain.event.dto.response.GetAvailableDatesResponseDto;
import kyonggi.bookslyserver.domain.event.dto.response.GetTimeEventsResponseDto;
import kyonggi.bookslyserver.domain.event.entity.timeEvent.TimeEvent;
import kyonggi.bookslyserver.domain.event.repository.EmployTimeEventScheduleRepository;
import kyonggi.bookslyserver.domain.reservation.entity.ReservationSetting;
import kyonggi.bookslyserver.domain.reservation.repository.ReservationSettingRepository;
import kyonggi.bookslyserver.domain.shop.entity.BusinessSchedule.BusinessSchedule;
import kyonggi.bookslyserver.domain.shop.entity.BusinessSchedule.DayName;
import kyonggi.bookslyserver.domain.shop.entity.Employee.Employee;
import kyonggi.bookslyserver.domain.shop.entity.Employee.WorkSchedule;
import kyonggi.bookslyserver.domain.shop.entity.Shop.Shop;
import kyonggi.bookslyserver.domain.shop.repository.EmployeeRepository;
import kyonggi.bookslyserver.domain.shop.service.ShopService;
import kyonggi.bookslyserver.global.error.exception.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    private final EmployTimeEventScheduleRepository employTimeEventScheduleRepository;
    private final ReservationSettingRepository reservationSettingRepository;

    public GetTimeEventsResponseDto getTimeEvents(Long shopId, Long employeeId, LocalDate date, Long ownerId) {
        Shop shop = shopService.findShop(shopId, ownerId);
        Employee employee = employeeRepository.findByIdAndShopId(employeeId, shop.getId())
                .orElseThrow(() -> new EntityNotFoundException(EMPLOYEE_NOT_FOUND));

        if (date == null) date = LocalDate.now();

        LocalDateTime dateTimeStart = date.atStartOfDay();
        LocalDateTime dateTimeEnd = date.atTime(LocalTime.MAX);

        Optional<List<TimeEvent>> timeEvents = employTimeEventScheduleRepository.findTimeEventsByEmployeeIdAndDateTime(employee.getId(), dateTimeStart, dateTimeEnd);
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
            log.info(dayName+"==============");
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

}
