package kyonggi.bookslyserver.domain.shop.dto.response.employee;

import kyonggi.bookslyserver.domain.event.entity.Event;
import kyonggi.bookslyserver.domain.shop.entity.Menu.Menu;
import lombok.Builder;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Builder
public record GetEventMenusResponseDto(
        List<EventWithMenusByCategoryResponseDto> events
) {
    public static GetEventMenusResponseDto of(Map<Event, List<Menu>> eventMenus) {
        Set<Event> eventSet = eventMenus.keySet();
        return GetEventMenusResponseDto.builder()
                .events(eventSet.stream().map(event ->
                        EventWithMenusByCategoryResponseDto.of(event.getTitle(), event.getDiscountRate(),eventMenus.get(event))).collect(Collectors.toList()))
                .build();

    }
}
