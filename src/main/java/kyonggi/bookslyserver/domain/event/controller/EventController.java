package kyonggi.bookslyserver.domain.event.controller;

import kyonggi.bookslyserver.domain.event.dto.request.CreateClosingEventRequestDto;
import kyonggi.bookslyserver.domain.event.dto.request.CreateTimeEventsRequestDto;
import kyonggi.bookslyserver.domain.event.dto.response.CreateClosingEventResponseDto;
import kyonggi.bookslyserver.domain.event.dto.response.CreateTimeEventsResponseDto;
import kyonggi.bookslyserver.domain.event.repository.ClosingEventRepository;
import kyonggi.bookslyserver.domain.event.service.ClosingEventService;
import kyonggi.bookslyserver.domain.event.service.TimeEventService;
import kyonggi.bookslyserver.global.auth.principal.shopOwner.OwnerId;
import kyonggi.bookslyserver.global.common.SuccessResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/events")
@Slf4j
public class EventController {

    private final TimeEventService timeEventService;
    private final ClosingEventService closingEventService;

    @PostMapping("/time-event")
    public ResponseEntity<SuccessResponse<?>> createTimeEvents(@OwnerId Long ownerId, @RequestBody CreateTimeEventsRequestDto createTimeEventsRequestDto){
        CreateTimeEventsResponseDto timeEventsResponseDto = timeEventService.createTimeEvents(ownerId, createTimeEventsRequestDto);
        return SuccessResponse.ok(timeEventsResponseDto);
    }

    @PostMapping("/closing-events")
    public ResponseEntity<SuccessResponse<?>> createClosingEvent(@RequestBody CreateClosingEventRequestDto createClosingEventRequestDto) {
        CreateClosingEventResponseDto createClosingEventResponseDto = closingEventService.createClosingEvent(createClosingEventRequestDto);
        return SuccessResponse.created(createClosingEventResponseDto);
    }
}
