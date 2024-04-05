package kyonggi.bookslyserver.domain.shop.entity.Shop;

import jakarta.persistence.*;
import kyonggi.bookslyserver.domain.shop.constant.categoryName;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="category_id")
    private Long id;


    @Enumerated(EnumType.STRING)
    private categoryName categoryName;


}
