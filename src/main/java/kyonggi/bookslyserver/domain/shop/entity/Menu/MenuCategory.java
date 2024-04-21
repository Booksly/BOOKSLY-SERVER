package kyonggi.bookslyserver.domain.shop.entity.Menu;

import jakarta.persistence.*;
import kyonggi.bookslyserver.domain.shop.dto.request.MenuCreateRequestDto;
import kyonggi.bookslyserver.global.common.BaseTimeEntity;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

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

    @OneToMany(mappedBy = "menuCategory", cascade = CascadeType.ALL)
    private List<Menu> menus = new ArrayList<>();


    public static MenuCategory createEntity(MenuCreateRequestDto requestDto){
        return MenuCategory.builder().name(requestDto.menuCategory()).menus(new ArrayList<>()).build();
    }

    public void addMenu(Menu menu){
        this.menus.add(menu);
        menu.setMenuCategory(this);
    }
}
