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

    @OneToMany(mappedBy = "menu", cascade = CascadeType.REMOVE)
    private List<MenuImage> menuImages = new ArrayList<>();

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
    public static Menu createEntity(MenuCreateRequestDto requestDto, List<MenuImage> menuImages){
        Menu menu = Menu
                .builder()
                .menuName(requestDto.menuName())
                .price(requestDto.price())
                .description(requestDto.description())
                .menuCategory(MenuCategory.builder().name(requestDto.menuCategory()).build())
                .menuImages(menuImages).build();

        for(MenuImage image : menuImages){
            image.setMenu(menu);
        }
        return menu;
    }

    public void register(Shop shop){
        shop.getMenus().add(this);
        this.shop = shop;
    }



    public List<String> update(MenuCreateRequestDto requestDto){
        this.menuName = requestDto.menuName();
        this.price = requestDto.price();
        this.description = requestDto.description();
        changeCategory(requestDto.menuCategory());

        for(int i = requestDto.menuImgUri().size() - 1; i >= 0; i--){
            this.menuImages.add(MenuImage.builder().menuImgUri(requestDto.menuImgUri().get(i)).build());
        }

        for(int j = 0; j < this.menuImages.size(); j++){
            this.menuImages.get(j).setMenu(this);
        }

        return requestDto.menuImgUri();
    }


    public void changeCategory(String menuCategory){
        this.menuCategory.setName(menuCategory);

    }


    public static Menu createEntity(Shop shop, MenuCreateRequestDto requestDto){
        List<MenuImage> images = new ArrayList<>();
        for(String img : requestDto.menuImgUri()){
            images.add(MenuImage.builder().menuImgUri(img).build());
        }
        return Menu.builder().menuName(requestDto.menuName()).price(requestDto.price()).description(requestDto.description()).shop(shop).menuImages(images).build();
    }

    public void addImg(List<MenuImage> images){
        for(MenuImage menuImage : images){
            menuImage.setMenu(this);
        }
    }


}
