package kyonggi.bookslyserver.domain.shop.entity.Employee;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import kyonggi.bookslyserver.domain.event.entity.timeEvent.TimeEventSchedule;
import kyonggi.bookslyserver.domain.event.entity.timeEvent.EmployeeTimeEvent;
import kyonggi.bookslyserver.domain.review.entity.Review;
import kyonggi.bookslyserver.domain.shop.dto.request.employee.EmployeeCreateRequestDto;
import kyonggi.bookslyserver.domain.shop.entity.Menu.Menu;
import kyonggi.bookslyserver.domain.shop.entity.Shop.Shop;
import kyonggi.bookslyserver.global.common.BaseTimeEntity;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.CascadeType.PERSIST;

@Entity
@Getter
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

    @OneToMany(mappedBy = "employee", cascade = PERSIST)
    private List<Review> reviews = new ArrayList<>();

    @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL)
    @Builder.Default
    private List<EmployeeMenu> employeeMenus = new ArrayList<>();

    @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL)
    private List<EmployeeTimeEvent> employeeTimeEvents = new ArrayList<>();

    @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL)
    private List<WorkSchedule> workSchedules = new ArrayList<>();

    @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL)
    private List<TimeEventSchedule> timeEventSchedules = new ArrayList<>();

    public void updateProfileImgUrl(String profileImgUri) {
        this.profileImgUri = profileImgUri;
    }

    public void updateName(String name) {
        this.name = name;
    }

    public void updateSelfIntro(String selfIntro) {
        this.selfIntro = selfIntro;
    }

    public void deleteEmployeeMenu(EmployeeMenu employeeMenu) {
        this.employeeMenus.remove(employeeMenu);
    }

    public void deleteAllEmployeeMenu() {
        this.employeeMenus = new ArrayList<>();
    }

}
