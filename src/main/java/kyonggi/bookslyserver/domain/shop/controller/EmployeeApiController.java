package kyonggi.bookslyserver.domain.shop.controller;

import kyonggi.bookslyserver.domain.shop.dto.request.EmployeeCreateRequestDto;
import kyonggi.bookslyserver.domain.shop.service.EmployeeService;
import kyonggi.bookslyserver.global.common.SuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class EmployeeApiController {
    final private EmployeeService employeeService;

    @PostMapping("/api/employee/{shopId}")
    public ResponseEntity<SuccessResponse<?>> createEmployee(@PathVariable("shopId") Long id, @RequestBody @Validated EmployeeCreateRequestDto requestDto){
        Long result = employeeService.join(id, requestDto);
        return SuccessResponse.ok(result);
    }
}
