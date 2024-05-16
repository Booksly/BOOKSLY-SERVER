package kyonggi.bookslyserver.domain.shop.dto.response.employee;

import kyonggi.bookslyserver.domain.shop.entity.Employee.Employee;
import lombok.Data;

@Data
public class ReserveEmployeesDto {
    private Long id;

    private String profileImgUri;

    private String name;

    public ReserveEmployeesDto(Employee employee){
        this.id = employee.getId();
        this.profileImgUri = employee.getProfileImgUri();
        this.name = employee.getName();

    }
}
