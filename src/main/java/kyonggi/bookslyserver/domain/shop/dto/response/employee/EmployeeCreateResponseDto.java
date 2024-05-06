package kyonggi.bookslyserver.domain.shop.dto.response.employee;

import kyonggi.bookslyserver.domain.shop.entity.Employee.Employee;
import lombok.Data;

@Data
public class EmployeeCreateResponseDto {
    private Long id;

    public EmployeeCreateResponseDto(Employee employee){
        this.id = employee.getId();
    }
}
