package kyonggi.bookslyserver.domain.event.service;

import kyonggi.bookslyserver.domain.event.dto.response.GetClosingEventsResponseDto;
import kyonggi.bookslyserver.domain.event.entity.closeEvent.ClosingEvent;
import kyonggi.bookslyserver.domain.event.repository.ClosingEventRepository;
import kyonggi.bookslyserver.domain.shop.entity.Shop.Shop;
import kyonggi.bookslyserver.domain.shop.service.ShopService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ClosingEventQueryService {

    private final ShopService shopService;
    private final ClosingEventRepository closingEventRepository;

    public GetClosingEventsResponseDto getClosingEvents(Long shopId, Long ownerId) {

        Shop shop = shopService.findShop(ownerId, shopId);
        List<Long> employeeIds = shop.getEmployees().stream().map(employee -> employee.getId()).collect(Collectors.toList());
        Optional<List<ClosingEvent>> closingEvents = closingEventRepository.findByEmployeeIds(employeeIds);

        return GetClosingEventsResponseDto.of(closingEvents);
    }
}
