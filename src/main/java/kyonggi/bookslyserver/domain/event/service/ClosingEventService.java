package kyonggi.bookslyserver.domain.event.service;

import kyonggi.bookslyserver.domain.event.dto.request.CreateClosingEventRequestDto;
import kyonggi.bookslyserver.domain.event.dto.response.CreateClosingEventResponseDto;
import kyonggi.bookslyserver.domain.event.repository.ClosingEventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class ClosingEventService {
    private final ClosingEventRepository closingEventRepository;

    public CreateClosingEventResponseDto createClosingEvent(CreateClosingEventRequestDto createClosingEventRequestDto) {

        return null;
    }
}
