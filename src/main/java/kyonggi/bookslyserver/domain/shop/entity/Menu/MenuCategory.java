package kyonggi.bookslyserver.domain.shop.entity.Menu;

import jakarta.persistence.*;
import kyonggi.bookslyserver.domain.shop.dto.request.menu.MenuCategoryCreateDto;
import kyonggi.bookslyserver.domain.shop.dto.request.menu.MenuCreateRequestDto;
import kyonggi.bookslyserver.domain.shop.entity.Shop.Shop;
import kyonggi.bookslyserver.global.common.BaseTimeEntity;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.FetchType.LAZY;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class MenuCategory extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name="shop_id")
    private Shop shop;

    @OneToMany(mappedBy = "menuCategory", cascade = CascadeType.ALL)
    private List<Menu> menus = new ArrayList<>();


    public static MenuCategory createEntity(Shop shop, MenuCreateRequestDto requestDto){
        return MenuCategory.builder().name(requestDto.menuCategory()).menus(new ArrayList<>()).shop(shop).build();
    }

    public static MenuCategory createEntity(MenuCategoryCreateDto requestDto, Shop shop){
        return MenuCategory.builder().name(requestDto.categoryName()).shop(shop).build();
    }

    public void addMenu(Menu menu){
        this.menus.add(menu);
        menu.setMenuCategory(this);
    }
}
