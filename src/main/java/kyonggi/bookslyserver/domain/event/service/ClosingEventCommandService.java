package kyonggi.bookslyserver.domain.event.service;

import kyonggi.bookslyserver.domain.event.dto.request.CreateClosingEventRequestDto;
import kyonggi.bookslyserver.domain.event.dto.request.ApplyClosingEventsRequestDto;
import kyonggi.bookslyserver.domain.event.dto.response.CreateClosingEventResponseDto;
import kyonggi.bookslyserver.domain.event.dto.response.ApplyClosingEventsResponseDto;
import kyonggi.bookslyserver.domain.event.entity.closeEvent.ClosingEvent;
import kyonggi.bookslyserver.domain.event.entity.closeEvent.ClosingEventMenu;
import kyonggi.bookslyserver.domain.event.repository.ClosingEventRepository;
import kyonggi.bookslyserver.domain.reservation.entity.ReservationSchedule;
import kyonggi.bookslyserver.domain.reservation.repository.ReservationScheduleRepository;
import kyonggi.bookslyserver.domain.shop.entity.Employee.Employee;
import kyonggi.bookslyserver.domain.shop.entity.Shop.Shop;
import kyonggi.bookslyserver.domain.shop.repository.EmployeeRepository;
import kyonggi.bookslyserver.domain.shop.repository.MenuRepository;
import kyonggi.bookslyserver.domain.shop.service.ShopService;
import kyonggi.bookslyserver.global.error.exception.EntityNotFoundException;
import kyonggi.bookslyserver.global.error.exception.ForbiddenException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static kyonggi.bookslyserver.global.error.ErrorCode.*;

@Service
@Transactional
@RequiredArgsConstructor
public class ClosingEventCommandService {
    private final ClosingEventRepository closingEventRepository;
    private final MenuRepository menuRepository;
    private final EmployeeRepository employeeRepository;
    private final ShopService shopService;
    private final ReservationScheduleRepository reservationScheduleRepository;

    public CreateClosingEventResponseDto createClosingEvent(CreateClosingEventRequestDto createClosingEventRequestDto) {

        ClosingEvent closingEvent = ClosingEvent.builder()
                .eventMessage(createClosingEventRequestDto.message())
                .discountRate(createClosingEventRequestDto.discountRate())
                .employee(employeeRepository
                        .findById(createClosingEventRequestDto.employeeId())
                        .orElseThrow(()->new EntityNotFoundException(EMPLOYEE_NOT_FOUND))).build();


        createClosingEventRequestDto.menuIds().stream()
                .map(id -> menuRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(MENU_NOT_FOUND)))
                .forEach(menu -> {
                    ClosingEventMenu closingEventMenu = ClosingEventMenu.builder()
                            .menu(menu)
                            .closingEvent(closingEvent).build();
                    closingEventMenu.addClosingEvent(closingEvent);
                });

        ClosingEvent createdEvent = closingEventRepository.save(closingEvent);
        return CreateClosingEventResponseDto.of(createdEvent);
    }

    public ApplyClosingEventsResponseDto applyClosingEvents(ApplyClosingEventsRequestDto applyClosingEventsRequestDto, boolean isApply, Long ownerId) {
        Shop shop = shopService.findShop(ownerId, applyClosingEventsRequestDto.shopId());

        ReservationSchedule reservationSchedule = getReservationSchedule(applyClosingEventsRequestDto);

        if(reservationSchedule.getShop().getId() != shop.getId()) throw new ForbiddenException();

        if (isApply) {
            reservationSchedule.addClosingEvent(getClosingEvent(reservationSchedule));
        } else reservationSchedule.cancelClosingEvent();

        return ApplyClosingEventsResponseDto.of(reservationSchedule);
    }

    private ReservationSchedule getReservationSchedule(ApplyClosingEventsRequestDto applyClosingEventsRequestDto) {
        Long scheduleId = applyClosingEventsRequestDto.reservationScheduleId();
        ReservationSchedule reservationSchedule = reservationScheduleRepository.findById(scheduleId).orElseThrow(() -> new EntityNotFoundException(RESERVATION_SCHEDULE_NOT_FOUND));
        return reservationSchedule;
    }

    private ClosingEvent getClosingEvent(ReservationSchedule reservationSchedule) {
        Employee employee = reservationSchedule.getEmployee();
        ClosingEvent closingEvent = closingEventRepository.findByEmployee(employee).orElseThrow(() -> new EntityNotFoundException(CLOSING_EVENT_NOT_FOUND));
        return closingEvent;
    }
}
