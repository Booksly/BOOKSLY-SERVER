package kyonggi.bookslyserver.domain.shop.dto.response.employee;

import kyonggi.bookslyserver.domain.shop.dto.response.menu.ReadEmployeesMenusResponseDto;
import kyonggi.bookslyserver.domain.shop.entity.Employee.Employee;
import kyonggi.bookslyserver.domain.shop.entity.Menu.Menu;
import lombok.Builder;
import java.util.List;
import java.util.stream.Collectors;

@Builder
public record ReadEmployeeResponseDto(
        String employeeName,
        String description,
        String imgUri,
        List<ReadEmployeesMenusResponseDto> categories,
        List<ReadEmployeeWorkScheduleResponseDto> workSchedules
) {
    public static ReadEmployeeResponseDto of(Employee employee, List<ReadEmployeesMenusResponseDto> menusResponseDtos) {

        return ReadEmployeeResponseDto.builder()
                .employeeName(employee.getName())
                .description(employee.getSelfIntro())
                .imgUri(employee.getProfileImgUri())
                .categories(menusResponseDtos)
                .workSchedules(employee.getWorkSchedules().stream().map(workSchedule -> ReadEmployeeWorkScheduleResponseDto.of(workSchedule)).collect(Collectors.toList()))
                .build();
    }
}
