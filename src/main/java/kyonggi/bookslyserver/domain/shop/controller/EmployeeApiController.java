package kyonggi.bookslyserver.domain.shop.controller;


import kyonggi.bookslyserver.domain.shop.dto.request.employee.EmployeeCreateRequestDto;
import kyonggi.bookslyserver.domain.shop.dto.response.employee.*;
import kyonggi.bookslyserver.domain.shop.service.EmployeeService;
import kyonggi.bookslyserver.global.common.SuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Validated
public class EmployeeApiController {

    private final EmployeeService employeeService;

    @PostMapping("/api/shops/{shopId}/employees")
    public ResponseEntity<SuccessResponse<?>> createEmployee(@PathVariable("shopId") Long shopId,
                                                             @PathVariable("assignAllMenus") Boolean assignAllMenus,
                                                             @RequestBody @Validated EmployeeCreateRequestDto requestDto) {
        EmployeeCreateResponseDto result = employeeService.join(shopId, assignAllMenus, requestDto);
        return SuccessResponse.ok(result);
    }

    @GetMapping("/api/shops/{shopId}/employees")
    public ResponseEntity<SuccessResponse<?>> readEmployee(@PathVariable("shopId") Long id, @RequestParam("withReviews") Boolean withReviews) {
        List<EmployeeReadDto> result = employeeService.readEmployee(id, withReviews);
        return SuccessResponse.ok(result);
    }

    @GetMapping("/api/shops/employees/{employeeId}")
    public ResponseEntity<SuccessResponse<?>> readOneEmployee(@PathVariable("employeeId") Long id) {
        EmployeeReadOneDto result = employeeService.readOneEmployee(id);
        return SuccessResponse.ok(result);
    }

    @GetMapping("/api/shops/{shopId}/employees/reservation")
    public ResponseEntity<SuccessResponse<?>> readReserveEmployees(@PathVariable("shopId") Long id) {
        List<ReserveEmployeesDto> result = employeeService.readReserveEmployees(id);
        return SuccessResponse.ok(result);
    }

    @GetMapping("/api/shops/{shopId}/employees/eventRegistration")
    public ResponseEntity<SuccessResponse<?>> readEmployeeNames(@PathVariable("shopId") Long id) {
        List<EventRegisterEmployeeNamesDto> result = employeeService.readEmployeeNames(id);
        return SuccessResponse.ok(result);
    }

    @PutMapping("/api/shops/employees/{employeeId}")
    public ResponseEntity<SuccessResponse<?>> updateEmployee(@PathVariable("employeeId") Long id, @RequestBody @Validated EmployeeCreateRequestDto requestDto) {
        EmployeeUpdateResponseDto result = employeeService.update(id, requestDto);
        return SuccessResponse.ok(result);
    }

    @DeleteMapping("/api/shops/employees/{employeeId}")
    public ResponseEntity<SuccessResponse<?>> deleteEmployee(@PathVariable("employeeId") Long id) {
        EmployeeDeleteResponseDto result = employeeService.delete(id);
        return SuccessResponse.ok(result);
    }

    @GetMapping("/api/shops/{shopId}/employees/{employeeId}/calendar-dates")
    public ResponseEntity<SuccessResponse<?>> getCalendarDates(@PathVariable("shopId") Long shopId, @PathVariable("employeeId") Long employeeId) {
        GetCalendarDatesResponseDto getCalendarDatesResponseDto = employeeService.getCalendarDates(shopId, employeeId);
        return SuccessResponse.ok(getCalendarDatesResponseDto);
    }

    @GetMapping("/api/shops/{shopId}/employees/{employeeId}/events/status")
    public ResponseEntity<SuccessResponse<?>> checkReservationScheduleEventsStatus(@PathVariable("shopId")Long shopId,
                                                                                   @PathVariable("employeeId")Long employeeId,
                                                                                   @RequestParam("schedule") Long reservationScheduleId) {
        CheckEventStatusResponseDto checkEventStatusResponseDto = employeeService.checkEventStatus(shopId, employeeId, reservationScheduleId);
        return SuccessResponse.ok(checkEventStatusResponseDto);
    }

    @GetMapping("/api/shops/{shopId}/employees/{employeeId}/events")
    public ResponseEntity<SuccessResponse<?>> getEventMenus(@PathVariable("shopId")Long shopId,
                                                            @PathVariable("employeeId")Long employeeId,
                                                            @RequestParam("schedule") Long reservationScheduleId) {
        GetEventMenusResponseDto getEventMenusResponseDto = employeeService.getEventMenus(shopId, employeeId, reservationScheduleId);
        return SuccessResponse.ok(getEventMenusResponseDto);
    }
}
