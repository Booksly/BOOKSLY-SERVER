package kyonggi.bookslyserver.domain.shop.entity.Menu;

import jakarta.persistence.*;
import kyonggi.bookslyserver.domain.event.entity.closeEvent.ClosingEventMenu;
import kyonggi.bookslyserver.domain.event.entity.timeEvent.TimeEventMenu;
import kyonggi.bookslyserver.domain.reservation.entity.ReservationMenu;
import kyonggi.bookslyserver.domain.shop.dto.request.menu.MenuCreateRequestDto;
import kyonggi.bookslyserver.domain.shop.entity.Employee.EmployeeMenu;
import kyonggi.bookslyserver.domain.shop.entity.Shop.Shop;
import kyonggi.bookslyserver.global.common.BaseTimeEntity;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.FetchType.LAZY;

@Entity
@Getter
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

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name="shop_id")
    private Shop shop;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name="menuCategory_id")
    private MenuCategory menuCategory;

    @OneToOne(mappedBy = "menu", cascade = CascadeType.REMOVE, fetch = LAZY)
    private MenuImage menuImage;

    @OneToMany(mappedBy = "menu", cascade = CascadeType.REMOVE)
    @Builder.Default
    private List<TimeEventMenu> timeEventMenus = new ArrayList<>();

    @OneToMany(mappedBy = "menu", cascade = CascadeType.REMOVE)
    private List<ClosingEventMenu> closingEventMenus = new ArrayList<>();

    @OneToMany(mappedBy = "menu", cascade = CascadeType.REMOVE)
    private List<EmployeeMenu> employeeMenus = new ArrayList<>();

    @OneToMany(mappedBy = "menu", cascade = CascadeType.REMOVE)
    private List<ReservationMenu> reservationMenus = new ArrayList<>();

    //==생성메서드==//
    public static Menu createEntity(MenuCreateRequestDto requestDto){
        Menu menu = Menu.builder()
                .menuName(requestDto.menuName())
                .price(requestDto.price())
                .description(requestDto.description())
                .build();

        return menu;
    }

    public void register(Shop shop){
        shop.getMenus().add(this);
        this.shop = shop;
    }

    public void addMenuCategory(MenuCategory menuCategory) {
        this.menuCategory = menuCategory;
        menuCategory.getMenus().add(this);
    }

    public void addShop(Shop shop) {
        this.shop = shop;
        shop.getMenus().add(this);
    }

    public void addImg(MenuImage image){
        this.menuImage = image;
    }

    public void updateMenuImage(String menuPictureUrl) {
        this.menuImage.updateImgUri(menuPictureUrl);
    }

    public void updateDescription(String description) {
        this.description = description;
    }

    public void updatePrice(Integer price) {
        this.price = price;
    }

    public void updateMenuCategory(MenuCategory menuCategory) {
        if (this.menuCategory != null) {
            this.menuCategory.getMenus().remove(this);
        }
        this.menuCategory = menuCategory;
        menuCategory.getMenus().add(this);
    }

    public void updateMenuName(String menuName) {
        this.menuName = menuName;
    }
}
