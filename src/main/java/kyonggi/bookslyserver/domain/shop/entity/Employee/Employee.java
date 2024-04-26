package kyonggi.bookslyserver.domain.shop.entity.Employee;

import jakarta.persistence.*;
import kyonggi.bookslyserver.domain.event.entity.timeEvent.EmployeeTimeEvent;
import kyonggi.bookslyserver.domain.review.entity.Review;
import kyonggi.bookslyserver.domain.shop.dto.request.EmployeeCreateRequestDto;
import kyonggi.bookslyserver.domain.shop.entity.Menu.Menu;
import kyonggi.bookslyserver.domain.shop.entity.Shop.Shop;
import kyonggi.bookslyserver.global.common.BaseTimeEntity;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@ToString(of = {"name", "selfIntro"})
public class Employee extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String profileImgUri;

    private String name;

    private String selfIntro;

    private int schedulingCycle;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shop_id")
    private Shop shop;

    @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL)
    private List<Review> reviews = new ArrayList<>();

    @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL)
    private List<EmployeeMenu> employeeMenus = new ArrayList<>();

    @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL)
    private List<EmployeeTimeEvent> employeeTimeEvents = new ArrayList<>();

    @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL)
    private List<WorkSchedule> workSchedules = new ArrayList<>();

    public static Employee createEntity(Shop shop, EmployeeCreateRequestDto requestDto){
        return Employee.builder()
                .name(requestDto.employeeName())
                .selfIntro(requestDto.description())
                .profileImgUri(requestDto.imgUri())
                .shop(shop)
                .employeeMenus(new ArrayList<>())
                .workSchedules(new ArrayList<>())
                .build();
    }

    public EmployeeMenu addMenu(Employee employee, Menu menu){
        EmployeeMenu employeeMenu = EmployeeMenu.createEntity(employee, menu);
        this.employeeMenus.add(employeeMenu);
        menu.getEmployeeMenus().add(employeeMenu);
        return employeeMenu;
    }

}
