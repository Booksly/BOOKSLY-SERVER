package kyonggi.bookslyserver.domain.shop.entity.Employee;


import jakarta.persistence.*;
import kyonggi.bookslyserver.domain.shop.entity.Employee.Employee;
import kyonggi.bookslyserver.domain.shop.entity.Menu.Menu;
import kyonggi.bookslyserver.global.common.BaseTimeEntity;
import lombok.*;

import java.util.ArrayList;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class EmployeeMenu extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="employee_id")
    private Employee employee;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="menu_id")
    private Menu menu;

    public static EmployeeMenu createEntity(Employee employee, Menu menu){
        EmployeeMenu employeeMenu = EmployeeMenu.builder()
                .employee(employee)
                .menu(menu).build();

        employee.getEmployeeMenus().add(employeeMenu);
        return employeeMenu;
    }
}
