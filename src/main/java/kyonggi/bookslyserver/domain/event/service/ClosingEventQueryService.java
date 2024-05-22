package kyonggi.bookslyserver.domain.event.service;

import kyonggi.bookslyserver.domain.event.dto.response.GetClosingEventsResponseDto;
import kyonggi.bookslyserver.domain.event.dto.response.GetTodayClosingEventsResponseDto;
import kyonggi.bookslyserver.domain.event.entity.closeEvent.ClosingEvent;
import kyonggi.bookslyserver.domain.event.repository.ClosingEventRepository;
import kyonggi.bookslyserver.domain.reservation.entity.ReservationSchedule;
import kyonggi.bookslyserver.domain.reservation.repository.ReservationScheduleRepository;
import kyonggi.bookslyserver.domain.reservation.service.ReserveQueryService;
import kyonggi.bookslyserver.domain.shop.entity.Shop.Shop;
import kyonggi.bookslyserver.domain.shop.service.ShopService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class ClosingEventQueryService {

    private final ShopService shopService;
    private final ClosingEventRepository closingEventRepository;
    private final ReservationScheduleRepository reservationScheduleRepository;
    private final ReserveQueryService reserveQueryService;

    public GetClosingEventsResponseDto getClosingEvents(Long shopId, Long ownerId) {

        Shop shop = shopService.findShop(ownerId, shopId);
        List<Long> employeeIds = shop.getEmployees().stream().map(employee -> employee.getId()).collect(Collectors.toList());
        Optional<List<ClosingEvent>> closingEvents = closingEventRepository.findByEmployeeIds(employeeIds);

        return GetClosingEventsResponseDto.of(closingEvents);
    }


    private List<ReservationSchedule> getTimeSlotFilteredEventSchedules(List<String> timeSlots, List<Shop> shops) {
        List<ReservationSchedule> earliestEventSchedulesForTimeSlot = new ArrayList<>();
        Pageable firstResult = PageRequest.of(0, 1);
        LocalDate nowDate = LocalDate.now();

        timeSlots.forEach(timeSlot -> {
            String[] times = timeSlot.split("-");
            LocalTime startTime = LocalTime.parse(times[0]);
            LocalTime endTime = LocalTime.parse(times[1]);

            shops.stream()
                    .map(shop -> reservationScheduleRepository.findWithAppliedClosingEvent(nowDate, startTime, endTime, shop, firstResult))
                    .filter(reservationSchedules -> !reservationSchedules.isEmpty())
                    .forEach(reservationSchedules -> earliestEventSchedulesForTimeSlot.add(reservationSchedules.get(0)));
        });

        return earliestEventSchedulesForTimeSlot;
    }


    public GetTodayClosingEventsResponseDto getTodayClosingEvents(List<String> regions, List<String> timeSlots, List<Long> categories) {
        List<Shop> shops = closingEventRepository.findShopsByClosingEvent();

        //지역 필터링 조건 적용
        if (regions != null && !regions.isEmpty()) shops = shopService.getRegionFilteredShops(regions, shops);

        //카테고리 필터링 조건 적용
        if (categories != null && !categories.isEmpty()) shops = shopService.getCategoryFilteredShops(categories, shops);

        if (timeSlots == null || timeSlots.isEmpty()){
            String timeSlot = LocalTime.now() + "-" + LocalTime.MAX;
            timeSlots = new ArrayList<>();
            timeSlots.add(timeSlot);
        }
        List<ReservationSchedule> timeSlotFilteredEventSchedules = getTimeSlotFilteredEventSchedules(timeSlots, shops);

        return GetTodayClosingEventsResponseDto.of(reserveQueryService.sortSchedulesByStartTimeThenRating(timeSlotFilteredEventSchedules));
    }

}
