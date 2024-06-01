package kyonggi.bookslyserver.domain.shop.controller;


import kyonggi.bookslyserver.domain.shop.dto.request.employee.EmployeeCreateRequestDto;
import kyonggi.bookslyserver.domain.shop.dto.request.employee.EmployeeUpdateRequestDto;
import kyonggi.bookslyserver.domain.shop.dto.response.employee.*;
import kyonggi.bookslyserver.domain.shop.service.EmployeeService;
import kyonggi.bookslyserver.global.common.SuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@Validated
public class EmployeeApiController {

    private final EmployeeService employeeService;

    @PostMapping("/api/shops/{shopId}/employees")
    public ResponseEntity<SuccessResponse<?>> createEmployee(@PathVariable("shopId") Long shopId,
                                                             @RequestParam("assignAllMenus") Boolean assignAllMenus,
                                                             @RequestPart @Validated EmployeeCreateRequestDto requestDto,
                                                             @RequestPart(required = false) MultipartFile profileImage) {
        EmployeeCreateResponseDto result = employeeService.join(shopId, assignAllMenus, requestDto, profileImage);
        return SuccessResponse.ok(result);
    }

    @GetMapping("/api/shops/{shopId}/employees")
    public ResponseEntity<SuccessResponse<?>> readEmployeesWithReviews(@PathVariable("shopId") Long shopId, @RequestParam("withReviews") Boolean withReviews) {
        ReadEmployeeWithReviewsWrapperResponseDto responseDto = employeeService.readEmployeesWithReviews(shopId, withReviews);
        return SuccessResponse.ok(responseDto);
    }

    @GetMapping("/api/shops/{shopId}/employees/names")
    public ResponseEntity<SuccessResponse<?>> readEmployeeNameWithImages(@PathVariable("shopId") Long shopId, @RequestParam("withImage") Boolean withImage) {
        ReadEmployeeNamesWithImageWrapperResponseDto responseDto = employeeService.readEmployeeNamesWithImages(shopId, withImage);
        return SuccessResponse.ok(responseDto);
    }

    @GetMapping("/api/shops/employees/{employeeId}")
    public ResponseEntity<SuccessResponse<?>> readOneEmployee(@PathVariable("employeeId") Long id) {
        EmployeeReadOneDto result = employeeService.readOneEmployee(id);
        return SuccessResponse.ok(result);
    }

    @PutMapping("/api/shops/{shopId}/employees/{employeeId}")
    public ResponseEntity<SuccessResponse<?>> updateEmployee(@PathVariable("shopId") Long shopId,
                                                             @PathVariable("employeeId") Long employeeId,
                                                             @RequestPart @Validated EmployeeUpdateRequestDto requestDto,
                                                             @RequestParam("assignAllMenus") Boolean assignAllMenus,
                                                             @RequestPart(required = false) MultipartFile profileImage) {
        UpdateEmployeeResponseDto result = employeeService.update(shopId, employeeId, assignAllMenus, requestDto, profileImage);
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
