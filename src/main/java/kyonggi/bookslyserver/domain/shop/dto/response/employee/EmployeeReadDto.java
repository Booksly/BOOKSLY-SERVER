package kyonggi.bookslyserver.domain.shop.dto.response.employee;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.Null;
import kyonggi.bookslyserver.domain.shop.entity.Employee.Employee;
import lombok.Data;
import org.apache.commons.lang3.ObjectUtils;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EmployeeReadDto {
    private Long id;

    private String imgUri;

    private String name;

    private Integer reviewNum;

    private String description;

    public EmployeeReadDto(Employee employee, Boolean withReviews) {
        this.id = employee.getId();
        this.imgUri = employee.getProfileImgUri();
        this.name = employee.getName();

        if (withReviews) {
                this.reviewNum = employee.getReviews().size();
        }
        else{
            this.reviewNum = null;
        }

        this.description = employee.getSelfIntro();
    }

}
