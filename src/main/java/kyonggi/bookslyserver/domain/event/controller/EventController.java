package kyonggi.bookslyserver.domain.event.controller;

import kyonggi.bookslyserver.domain.event.dto.request.CreateTimeEventsRequestDto;
import kyonggi.bookslyserver.domain.event.dto.response.CreateTimeEventsResponseDto;
import kyonggi.bookslyserver.domain.event.service.TimeEventService;
import kyonggi.bookslyserver.global.auth.principal.shopOwner.OwnerId;
import kyonggi.bookslyserver.global.common.SuccessResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/events")
@Slf4j
public class EventController {

    private final TimeEventService timeEventService;

    @PostMapping("/time-events")
    public ResponseEntity<SuccessResponse<?>> createTimeEvents(@OwnerId Long ownerId, @RequestBody CreateTimeEventsRequestDto createTimeEventsRequestDto){
        CreateTimeEventsResponseDto timeEventsResponseDto = timeEventService.createTimeEvents(ownerId, createTimeEventsRequestDto);
        return SuccessResponse.ok(timeEventsResponseDto);
    }
}
