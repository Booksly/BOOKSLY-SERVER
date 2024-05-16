package kyonggi.bookslyserver.domain.shop.controller;

import kyonggi.bookslyserver.domain.shop.dto.request.EmployeeCreateRequestDto;
import kyonggi.bookslyserver.domain.shop.dto.response.employee.EmployeeDeleteResponseDto;
import kyonggi.bookslyserver.domain.shop.dto.response.employee.EmployeeCreateResponseDto;
import kyonggi.bookslyserver.domain.shop.dto.response.employee.EmployeeReadDto;
import kyonggi.bookslyserver.domain.shop.dto.response.employee.GetCalendarDatesResponseDto;
import kyonggi.bookslyserver.domain.shop.service.EmployeeService;
import kyonggi.bookslyserver.global.common.SuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class EmployeeApiController {
    final private EmployeeService employeeService;


    @GetMapping("/api/shops/{shopId}/employees")
    public ResponseEntity<SuccessResponse<?>> readEmployee(@PathVariable("shopId") Long id){
        List<EmployeeReadDto> result = employeeService.readEmployee(id);
        return SuccessResponse.ok(result);
    }

    @PostMapping("/api/shops/{shopId}/employees")
    public ResponseEntity<SuccessResponse<?>> createEmployee(@PathVariable("shopId") Long id, @RequestBody @Validated EmployeeCreateRequestDto requestDto){
        EmployeeCreateResponseDto result = employeeService.join(id, requestDto);
        return SuccessResponse.ok(result);
    }

    @PutMapping("/api/shops/employees/{employeeId}")
    public ResponseEntity<SuccessResponse<?>> updateEmployee(@PathVariable("employeeId") Long id, @RequestBody @Validated EmployeeCreateRequestDto requestDto){
        Long result = employeeService.update(id, requestDto);
        return SuccessResponse.ok(result);
    }

    @DeleteMapping("/api/shops/employees/{employeeId}")
    public ResponseEntity<SuccessResponse<?>> deleteEmployee(@PathVariable("employeeId") Long id){
        EmployeeDeleteResponseDto result = employeeService.delete(id);
        return SuccessResponse.ok(result);
    }

    @GetMapping("/api/shops/{shopId}/employees/{employeeId}/calendar-dates")
    public ResponseEntity<SuccessResponse<?>> getCalendarDates(@PathVariable("shopId") Long shopId, @PathVariable("employeeId") Long employeeId) {
        GetCalendarDatesResponseDto getCalendarDatesResponseDto = employeeService.getCalendarDates(shopId, employeeId);
        return SuccessResponse.ok(getCalendarDatesResponseDto);
    }
}
