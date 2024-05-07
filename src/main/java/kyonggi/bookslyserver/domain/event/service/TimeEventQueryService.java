package kyonggi.bookslyserver.domain.event.service;

import kyonggi.bookslyserver.domain.event.dto.response.GetTimeEventsResponseDto;
import kyonggi.bookslyserver.domain.event.entity.timeEvent.TimeEvent;
import kyonggi.bookslyserver.domain.event.repository.EmployTimeEventScheduleRepository;
import kyonggi.bookslyserver.domain.shop.entity.Employee.Employee;
import kyonggi.bookslyserver.domain.shop.entity.Shop.Shop;
import kyonggi.bookslyserver.domain.shop.repository.EmployeeRepository;
import kyonggi.bookslyserver.domain.shop.service.ShopService;
import kyonggi.bookslyserver.global.error.exception.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static kyonggi.bookslyserver.global.error.ErrorCode.EMPLOYEE_NOT_FOUND;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class TimeEventQueryService {

    private final ShopService shopService;
    private final EmployeeRepository employeeRepository;
    private final EmployTimeEventScheduleRepository employTimeEventScheduleRepository;

    public GetTimeEventsResponseDto getTimeEvents(Long shopId, Long employeeId, LocalDate date, Long ownerId) {
        Shop shop = shopService.findShop(shopId, ownerId);
        Employee employee = employeeRepository.findByIdAndShopId(employeeId, shop.getId())
                .orElseThrow(() -> new EntityNotFoundException(EMPLOYEE_NOT_FOUND));

        if (date == null) date = LocalDate.now();

        LocalDateTime dateTimeStart = date.atStartOfDay();
        LocalDateTime dateTimeEnd = date.atTime(LocalTime.MAX);

        Optional<List<TimeEvent>> timeEvents = employTimeEventScheduleRepository.findTimeEventsByEmployeeIdAndDateTime(employee.getId(), dateTimeStart, dateTimeEnd);
        timeEvents.get().stream().forEach(timeEvent -> log.info(timeEvent.getTitle()));
        return GetTimeEventsResponseDto.of(timeEvents);
    }
}
