package kyonggi.bookslyserver.domain.shop.dto.response.employee;

import kyonggi.bookslyserver.domain.shop.entity.Employee.Employee;
import lombok.Data;

@Data
public class EmployeeReadDto {
    private Long id;

    private String imgUri;

    private String name;

    private int reviewNum;

    private String description;

    public EmployeeReadDto(Employee employee){
        this.id = employee.getId();
        this.imgUri = employee.getProfileImgUri();
        this.name = employee.getName();
        if(employee.getReviews() != null){
            this.reviewNum = employee.getReviews().size();
        }
        else{
            this.reviewNum = 0;
        }
        this.description = employee.getSelfIntro();
    }
}
