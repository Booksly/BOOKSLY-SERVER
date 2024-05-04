package kyonggi.bookslyserver.domain.shop.dto.response.employee;

import kyonggi.bookslyserver.domain.shop.entity.Employee.Employee;
import lombok.Data;

@Data
public class EmployeeDeleteResponseDto {
    private Long id;

    public EmployeeDeleteResponseDto(Long id){
        this.id = id;
    }
}
