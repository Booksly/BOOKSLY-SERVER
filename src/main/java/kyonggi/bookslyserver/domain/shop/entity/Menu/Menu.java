package kyonggi.bookslyserver.domain.shop.entity.Menu;

import jakarta.persistence.*;
import kyonggi.bookslyserver.domain.event.entity.closeEvent.ClosingEventMenu;
import kyonggi.bookslyserver.domain.event.entity.timeEvent.TimeEventMenu;
import kyonggi.bookslyserver.domain.reservation.entity.ReservationMenu;
import kyonggi.bookslyserver.domain.shop.entity.Employee.EmployeeMenu;
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
@ToString(of = {"menu_name", "description", "price"})
public class Menu extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String menuName;

    @Lob
    private String description;

    private int price;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="shop_id")
    private Shop shop;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="menuCategory_id")
    private MenuCategory menuCategory;

    @OneToMany(mappedBy = "menu", cascade = CascadeType.ALL)
    private List<MenuImage> menuImages = new ArrayList<>();

    @OneToMany(mappedBy = "menu")
    private List<TimeEventMenu> timeEventMenus = new ArrayList<>();

    @OneToMany(mappedBy = "menu")
    private List<ClosingEventMenu> closingEventMenus = new ArrayList<>();

    @OneToMany(mappedBy = "menu")
    private List<EmployeeMenu> employeeMenus = new ArrayList<>();

    @OneToMany(mappedBy = "menu")
    private List<ReservationMenu> reservationMenus = new ArrayList<>();
}
