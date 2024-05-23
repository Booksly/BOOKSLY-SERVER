package kyonggi.bookslyserver.domain.event.service;

import kyonggi.bookslyserver.domain.event.dto.request.CreateClosingEventRequestDto;
import kyonggi.bookslyserver.domain.event.dto.request.ApplyClosingEventsRequestDto;
import kyonggi.bookslyserver.domain.event.dto.response.CreateClosingEventResponseDto;
import kyonggi.bookslyserver.domain.event.dto.response.ApplyClosingEventsResponseDto;
import kyonggi.bookslyserver.domain.event.dto.response.DeleteClosingEventResponseDto;
import kyonggi.bookslyserver.domain.event.entity.closeEvent.ClosingEvent;
import kyonggi.bookslyserver.domain.event.entity.closeEvent.ClosingEventMenu;
import kyonggi.bookslyserver.domain.event.repository.ClosingEventRepository;
import kyonggi.bookslyserver.domain.reservation.entity.ReservationSchedule;
import kyonggi.bookslyserver.domain.reservation.repository.ReservationScheduleRepository;
import kyonggi.bookslyserver.domain.shop.entity.Employee.Employee;
import kyonggi.bookslyserver.domain.shop.entity.Menu.Menu;
import kyonggi.bookslyserver.domain.shop.entity.Shop.Shop;
import kyonggi.bookslyserver.domain.shop.repository.EmployeeMenuRepository;
import kyonggi.bookslyserver.domain.shop.repository.EmployeeRepository;
import kyonggi.bookslyserver.domain.shop.repository.MenuRepository;
import kyonggi.bookslyserver.domain.shop.service.EmployeeService;
import kyonggi.bookslyserver.domain.shop.service.ShopService;
import kyonggi.bookslyserver.global.error.exception.ConflictException;
import kyonggi.bookslyserver.global.error.exception.EntityNotFoundException;
import kyonggi.bookslyserver.global.error.exception.ForbiddenException;
import kyonggi.bookslyserver.global.error.exception.InvalidValueException;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static kyonggi.bookslyserver.global.error.ErrorCode.*;

@Service
@Transactional
@RequiredArgsConstructor
public class ClosingEventCommandService {
    private final ClosingEventRepository closingEventRepository;
    private final MenuRepository menuRepository;
    private final EmployeeService employeeService;
    private final ShopService shopService;
    private final ReservationScheduleRepository reservationScheduleRepository;
    private final EmployeeMenuRepository employeeMenuRepository;

    private Employee findEmployee(Long employeeId, Long shopId) {
        Employee employee = employeeService.findEmployeeById(employeeId);
        employeeService.validateBelongShop(employeeId,shopId);

        return employee;
    }

    private List<Menu> getMenus(CreateClosingEventRequestDto createClosingEventRequestDto, Employee employee) {
        List<Menu> menus = createClosingEventRequestDto.menuIds().stream()
                .map(id -> menuRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(MENU_NOT_FOUND))).collect(Collectors.toList());

        int employeeMenuNum = employeeMenuRepository.findByMenuAndEmployee(menus, employee);
        if (employeeMenuNum != menus.size()) {
            throw new InvalidValueException(MENU_IS_NOT_EMPLOYEEMENU);
        }
        return menus;
    }

    public CreateClosingEventResponseDto createClosingEvent(CreateClosingEventRequestDto createClosingEventRequestDto, Long ownerId) {
        Shop shop = shopService.findShop(ownerId, createClosingEventRequestDto.shopId());
        Employee employee = findEmployee(createClosingEventRequestDto.employeeId(), shop.getId());

        if (closingEventRepository.existsByEmployeeId(employee.getId()))
            throw new ConflictException(EVENT_SETTING_ALREADY_EXISTS);

        ClosingEvent closingEvent = ClosingEvent.builder()
                .eventMessage(createClosingEventRequestDto.message())
                .discountRate(createClosingEventRequestDto.discountRate())
                .employee(employee).build();

        List<Menu> menus = getMenus(createClosingEventRequestDto, employee);

        menus.forEach(menu -> {
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

    public DeleteClosingEventResponseDto deleteEvent(Long eventId, Long shopId, Long ownerId) {
        Shop shop = shopService.findShop(ownerId, shopId);
        ClosingEvent closingEvent = closingEventRepository.findById(eventId).orElseThrow(() -> new EntityNotFoundException(CLOSING_EVENT_NOT_FOUND));

        //가게 소유 마감이벤트가 아니면 불가
        if (!shop.getId().equals(closingEvent.getEmployee().getShop().getId())) {
            throw new ForbiddenException();}

        List<ReservationSchedule> reservationSchedules = reservationScheduleRepository.findByClosingEvent(closingEvent);
        reservationSchedules.stream().forEach(reservationSchedule -> reservationSchedule.cancelClosingEvent());

        closingEventRepository.delete(closingEvent);
        return DeleteClosingEventResponseDto.of(eventId);
    }
}
