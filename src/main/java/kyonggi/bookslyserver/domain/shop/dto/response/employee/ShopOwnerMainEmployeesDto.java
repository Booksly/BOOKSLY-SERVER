package kyonggi.bookslyserver.domain.shop.dto.response.employee;

import kyonggi.bookslyserver.domain.shop.entity.Employee.Employee;
import lombok.Data;

@Data
public class ShopOwnerMainEmployeesDto {
    private String imgUri;

    private String employeeName;

    private String intro;

    public ShopOwnerMainEmployeesDto(Employee employee){
        this.imgUri = employee.getProfileImgUri();
        this.employeeName = employee.getName();
        this.intro = employee.getSelfIntro();
    }
}
