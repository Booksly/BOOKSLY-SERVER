package kyonggi.bookslyserver.domain.event.service;

import kyonggi.bookslyserver.domain.event.dto.request.CreateTimeEventsRequestDto;
import kyonggi.bookslyserver.domain.event.dto.response.CreateTimeEventsResponseDto;
import kyonggi.bookslyserver.domain.event.repository.TimeEventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class TimeEventService {

    private final TimeEventRepository timeEventRepository;

    public CreateTimeEventsResponseDto createTimeEvents(Long ownerId, CreateTimeEventsRequestDto createTimeEventsRequestDto) {
        return null;
    }
}
