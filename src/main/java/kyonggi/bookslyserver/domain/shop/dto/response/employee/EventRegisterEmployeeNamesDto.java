package kyonggi.bookslyserver.domain.shop.dto.response.employee;

import kyonggi.bookslyserver.domain.shop.entity.Employee.Employee;
import lombok.Data;

@Data
public class EventRegisterEmployeeNamesDto {
    private Long id;
    private String name;
    public EventRegisterEmployeeNamesDto(Employee employee){
        this.id = employee.getId();
        this.name = employee.getName();
    }
}
