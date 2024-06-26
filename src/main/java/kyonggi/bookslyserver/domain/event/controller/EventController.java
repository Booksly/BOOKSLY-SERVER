package kyonggi.bookslyserver.domain.event.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import kyonggi.bookslyserver.domain.event.dto.request.CreateClosingEventRequestDto;
import kyonggi.bookslyserver.domain.event.dto.request.CreateLocalTimeEventsRequestDto;
import kyonggi.bookslyserver.domain.event.dto.request.CreateStringTimeEventsRequestDto;
import kyonggi.bookslyserver.domain.event.dto.request.ApplyClosingEventsRequestDto;
import kyonggi.bookslyserver.domain.event.dto.response.*;
import kyonggi.bookslyserver.domain.event.service.ClosingEventCommandService;
import kyonggi.bookslyserver.domain.event.service.ClosingEventQueryService;
import kyonggi.bookslyserver.domain.event.service.TimeEventCommandService;
import kyonggi.bookslyserver.domain.event.service.TimeEventQueryService;
import kyonggi.bookslyserver.domain.event.validation.annotation.TimeSlotFormat;
import kyonggi.bookslyserver.global.auth.principal.shopOwner.OwnerId;
import kyonggi.bookslyserver.global.common.SuccessResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/events")
@Slf4j
@Validated
public class EventController {

    private final TimeEventCommandService timeEventCommandService;
    private final TimeEventQueryService timeEventQueryService;
    private final ClosingEventCommandService closingEventCommandService;
    private final ClosingEventQueryService closingEventQueryService;

    @PostMapping("/time-events")
    public ResponseEntity<SuccessResponse<?>> createTimeEvents(@OwnerId Long ownerId, @RequestBody @Valid CreateStringTimeEventsRequestDto createTimeEventsRequestDto) {
        CreateTimeEventsResponseDto timeEventsResponseDto = timeEventCommandService.createTimeEvents(ownerId, CreateLocalTimeEventsRequestDto.of(createTimeEventsRequestDto));
        return SuccessResponse.ok(timeEventsResponseDto);
    }

    @GetMapping("/time-events")
    public ResponseEntity<SuccessResponse<?>> getTimeEvents(@RequestParam(value = "date", required = false) LocalDate date, @RequestParam("shop") @NotNull Long shopId,
                                                            @RequestParam("employee") @NotNull Long employeeId, @OwnerId Long ownerId) {
        GetTimeEventsResponseDto getTimeEventsResponseDto = timeEventQueryService.getTimeEvents(shopId, employeeId, date, ownerId);
        return SuccessResponse.ok(getTimeEventsResponseDto);
    }

    @GetMapping("/time-events/dates")
    public ResponseEntity<SuccessResponse<?>> getAvailableDates(@RequestParam("shop") Long shopId, @OwnerId Long ownerId) {
        GetAvailableDatesResponseDto getAvailableDatesResponseDto = timeEventQueryService.getAvailableDates(shopId, ownerId);
        return SuccessResponse.ok(getAvailableDatesResponseDto);
    }

    @DeleteMapping("/time-events/{timeEventId}")
    public ResponseEntity<SuccessResponse<?>> deleteTimeEvent(@RequestParam("shop") Long shopId, @PathVariable("timeEventId") Long timeEventId, @OwnerId Long ownerId) {
        DeleteTimeEventResponseDto deleteTimeEventResponseDto = timeEventCommandService.deleteEvent(shopId, timeEventId, ownerId);
        return SuccessResponse.ok(deleteTimeEventResponseDto);
    }

    @GetMapping("/time-events/today")
    public ResponseEntity<SuccessResponse<?>> getTodayTimeEvents(@RequestParam(value = "region", required = false) List<String> regions,
                                                                    @RequestParam(value = "time_slot", required = false) @TimeSlotFormat List<String> timeSlots,
                                                                    @RequestParam(value = "category",required = false)List<Long> categories) {

        GetTodayTimeEventsResponseDto getTodayTimeEventsResponseDto = timeEventQueryService.getTodayTimeEvents(regions, timeSlots, categories);
        return SuccessResponse.ok(getTodayTimeEventsResponseDto);
    }


    @PostMapping("/closing-events")
    public ResponseEntity<SuccessResponse<?>> createClosingEvent(@RequestBody CreateClosingEventRequestDto createClosingEventRequestDto, @OwnerId Long ownerId) {
        CreateClosingEventResponseDto createClosingEventResponseDto = closingEventCommandService.createClosingEvent(createClosingEventRequestDto, ownerId);
        return SuccessResponse.created(createClosingEventResponseDto);
    }

    @GetMapping("/closing-events")
    public ResponseEntity<SuccessResponse<?>> getClosingEvents(@RequestParam("shop") Long shopId, @OwnerId Long ownerId) {
        GetClosingEventsResponseDto getClosingEventsResponseDto = closingEventQueryService.getClosingEvents(shopId, ownerId);
        return SuccessResponse.ok(getClosingEventsResponseDto);
    }

    @PostMapping("/closing-events/schedules")
    public ResponseEntity<SuccessResponse<?>> applyClosingEvents(@RequestBody ApplyClosingEventsRequestDto applyClosingEventsRequestDto, @RequestParam("apply") boolean isApply, @OwnerId Long ownerId) {
        ApplyClosingEventsResponseDto applyClosingEventsResponseDto = closingEventCommandService.applyClosingEvents(applyClosingEventsRequestDto, isApply, ownerId);
        return SuccessResponse.ok(applyClosingEventsResponseDto);
    }

    @DeleteMapping("/closing-events/{closingEventId}")
    public ResponseEntity<SuccessResponse<?>> deleteClosingEvent(@PathVariable("closingEventId") Long eventId, @RequestParam("shop") Long shopId, @OwnerId Long ownerId) {
        DeleteClosingEventResponseDto deleteClosingEventResponseDto = closingEventCommandService.deleteEvent(eventId, shopId, ownerId);
        return SuccessResponse.ok(deleteClosingEventResponseDto);
    }

    @GetMapping("/closing-events/today")
    public ResponseEntity<SuccessResponse<?>> getTodayClosingEvents(@RequestParam(value = "region", required = false) List<String> regions,
                                                                    @RequestParam(value = "time_slot", required = false) @TimeSlotFormat List<String> timeSlots,
                                                                    @RequestParam(value = "category",required = false)List<Long> categories) {

        GetTodayClosingEventsResponseDto getTodayClosingEventsResponseDto = closingEventQueryService.getTodayClosingEvents(regions, timeSlots, categories);
        return SuccessResponse.ok(getTodayClosingEventsResponseDto);
    }
}
