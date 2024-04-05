package kyonggi.bookslyserver.domain.shop.entity.Menu;


import jakarta.persistence.*;
import kyonggi.bookslyserver.domain.shop.entity.Menu.Menu;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class MenuImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String menuImg_uri;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="menu_id")
    private Menu menu;
}
