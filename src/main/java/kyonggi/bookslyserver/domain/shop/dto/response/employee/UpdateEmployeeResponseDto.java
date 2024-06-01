package kyonggi.bookslyserver.domain.shop.dto.response.employee;

import kyonggi.bookslyserver.domain.shop.entity.Employee.Employee;
import kyonggi.bookslyserver.domain.shop.entity.Employee.EmployeeMenu;
import kyonggi.bookslyserver.domain.shop.entity.Menu.Menu;
import lombok.Builder;
import java.util.List;
import java.util.stream.Collectors;

@Builder
public record UpdateEmployeeResponseDto(
        String employeeName,
        String description,
        String imgUri,
        List<UpdateEmployeeMenusResponseDto> categories,
        List<UpdateEmployeeWorkScheduleResponseDto> workSchedules
){
    public static UpdateEmployeeResponseDto of(Employee employee) {
        List<Menu> menus = employee.getEmployeeMenus().stream().map(employeeMenu -> employeeMenu.getMenu()).collect(Collectors.toList());
        List<UpdateEmployeeMenusResponseDto> menusUpdateResponseDtos = menus.stream().collect(Collectors.groupingBy(Menu::getMenuCategory))
                .entrySet().stream()
                .map(entry -> UpdateEmployeeMenusResponseDto.of(entry.getKey().getName(), entry.getValue()))
                .collect(Collectors.toList());

        return UpdateEmployeeResponseDto.builder()
                .employeeName(employee.getName())
                .description(employee.getSelfIntro())
                .imgUri(employee.getProfileImgUri() != null ? employee.getProfileImgUri() : null)
                .categories(menusUpdateResponseDtos)
                .workSchedules(employee.getWorkSchedules().stream().map(workSchedule -> UpdateEmployeeWorkScheduleResponseDto.of(workSchedule)).collect(Collectors.toList()))
                .build();
    }

}
