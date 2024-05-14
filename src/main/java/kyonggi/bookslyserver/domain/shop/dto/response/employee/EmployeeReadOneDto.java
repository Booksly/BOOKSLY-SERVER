package kyonggi.bookslyserver.domain.shop.dto.response.employee;

import kyonggi.bookslyserver.domain.shop.entity.Employee.Employee;
import kyonggi.bookslyserver.domain.shop.entity.Employee.EmployeeMenu;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class EmployeeReadOneDto {

    private Long id;

    private String imgUrl;

    private String name;

    private String description;

    private List<EmployeeReadOneMenuDto> menus;

    private List<EmployeeReadOneWorkScheduleDto> workSchedules;

    public EmployeeReadOneDto(Employee employee, List<EmployeeMenu> employeeMenus){

        this.id = employee.getId();

        this.imgUrl = employee.getProfileImgUri();

        this.name = employee.getName();

        this.description = employee.getSelfIntro();
        if(employeeMenus != null) {
            this.menus = employeeMenus.stream().map(employeeMenu -> new EmployeeReadOneMenuDto(employeeMenu.getMenu())).collect(Collectors.toList());
        }
        this.workSchedules = employee.getWorkSchedules().stream().map(workSchedule -> new EmployeeReadOneWorkScheduleDto(workSchedule)).collect(Collectors.toList());
    }
}
