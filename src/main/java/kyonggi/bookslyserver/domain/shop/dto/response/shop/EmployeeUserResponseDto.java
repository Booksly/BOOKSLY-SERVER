package kyonggi.bookslyserver.domain.shop.dto.response.shop;

import kyonggi.bookslyserver.domain.shop.entity.Employee.Employee;
import lombok.Data;

@Data
public class EmployeeUserResponseDto {
    private String name;

    private String description;

    private String profileImgUri;

    private int reviewNum;

    public EmployeeUserResponseDto(Employee employee){
        this.name = employee.getName();
        this.description = employee.getSelfIntro();
        this.profileImgUri = employee.getProfileImgUri();
        if(employee.getReviews() != null) {
            this.reviewNum = employee.getReviews().size();
        }
        else{
            this.reviewNum = 0;
        }
    }


}
