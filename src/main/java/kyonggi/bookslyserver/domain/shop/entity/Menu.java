package kyonggi.bookslyserver.domain.shop.entity;

import jakarta.persistence.*;
import kyonggi.bookslyserver.global.common.BaseTimeEntity;
import lombok.*;

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
    @Column(name="menu_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="shop_id")
    private Shop shop;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="menu_category_id")
    private MenuCategory menuCategory;

    private String menu_name;

    @Lob
    private String description;

    private int price;

    /**
     * 추가필드
     *  1. 메뉴 이미지 참조 필드 추가
     *  2. 타임이벤트메뉴 참조 필드 추가
     *  3. 마감임박이벤트메뉴 참조 필드 추가
     *
     */
}
