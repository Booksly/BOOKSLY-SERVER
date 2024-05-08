package kyonggi.bookslyserver.domain.event.service;

import kyonggi.bookslyserver.domain.event.dto.request.CreateClosingEventRequestDto;
import kyonggi.bookslyserver.domain.event.dto.response.CreateClosingEventResponseDto;
import kyonggi.bookslyserver.domain.event.entity.closeEvent.ClosingEvent;
import kyonggi.bookslyserver.domain.event.entity.closeEvent.ClosingEventMenu;
import kyonggi.bookslyserver.domain.event.repository.ClosingEventRepository;
import kyonggi.bookslyserver.domain.shop.repository.EmployeeRepository;
import kyonggi.bookslyserver.domain.shop.repository.MenuRepository;
import kyonggi.bookslyserver.global.error.exception.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static kyonggi.bookslyserver.global.error.ErrorCode.EMPLOYEE_NOT_FOUND;
import static kyonggi.bookslyserver.global.error.ErrorCode.MENU_NOT_FOUND;

@Service
@Transactional
@RequiredArgsConstructor
public class ClosingEventCommandService {
    private final ClosingEventRepository closingEventRepository;
    private final MenuRepository menuRepository;
    private final EmployeeRepository employeeRepository;

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
}
