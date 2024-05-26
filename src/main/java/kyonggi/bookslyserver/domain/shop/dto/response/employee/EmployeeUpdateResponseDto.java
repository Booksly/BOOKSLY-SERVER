package kyonggi.bookslyserver.domain.shop.dto.response.employee;

import jakarta.validation.constraints.NotNull;
import kyonggi.bookslyserver.domain.shop.dto.request.employee.EmployeeCreateRequestDto;
import kyonggi.bookslyserver.domain.shop.dto.request.employee.EmployeeWorkScheduleDto;
import kyonggi.bookslyserver.domain.shop.entity.Employee.Employee;
import kyonggi.bookslyserver.domain.shop.entity.Employee.EmployeeMenu;
import kyonggi.bookslyserver.domain.shop.entity.Employee.WorkSchedule;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class EmployeeUpdateResponseDto {
    @NotNull String employeeName;
    @NotNull String description;
    @NotNull String imgUri;
    List<String> menus;

    @NotNull List<EmployeeWorkScheduleDto> workSchedules;

    public EmployeeUpdateResponseDto(EmployeeCreateRequestDto requestDto){
        this.employeeName = requestDto.employeeName();
        this.description = requestDto.description();
        this.imgUri = requestDto.imgUri();
        this.workSchedules = new ArrayList<>();

        if(requestDto.menus() != null){
            this.menus = new ArrayList<>();
            for(String menu : requestDto.menus()){
                this.menus.add(menu);
            }
        }

        this.workSchedules = requestDto.workSchedules();

    }

}
