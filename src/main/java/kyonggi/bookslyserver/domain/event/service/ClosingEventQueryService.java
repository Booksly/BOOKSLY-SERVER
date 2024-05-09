package kyonggi.bookslyserver.domain.event.service;

import kyonggi.bookslyserver.domain.event.dto.response.GetClosingEventsResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class ClosingEventQueryService {

    public GetClosingEventsResponseDto getClosingEvents(Long shopId, Long ownerId) {
        return null;
    }
}
