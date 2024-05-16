package kyonggi.bookslyserver.domain.shop.dto.response.employee;

import kyonggi.bookslyserver.domain.shop.entity.Employee.Employee;
import lombok.Data;

@Data
public class ReserveEmployeesDto {
    private Long id;

    private String name;

    public ReserveEmployeesDto(Employee employee){
        this.id = employee.getId();
        this.name = employee.getName();
    }
}
