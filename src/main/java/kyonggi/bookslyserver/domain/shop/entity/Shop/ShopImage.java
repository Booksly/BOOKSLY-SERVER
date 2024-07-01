package kyonggi.bookslyserver.domain.shop.entity.Shop;


import jakarta.persistence.*;
import kyonggi.bookslyserver.domain.shop.entity.Shop.Shop;
import kyonggi.bookslyserver.global.common.BaseTimeEntity;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ShopImage extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="shop_id")
    private Shop shop;

    private String imgUri;

    private Boolean isRepresentative;

    public void update(String imgUri, boolean isRepresentative){
        if(imgUri != null) {
            this.imgUri = imgUri;
        }
        if(isRepresentative != this.isRepresentative) {
            this.isRepresentative = isRepresentative;
        }
    }

    //== 연관관계 편의 메서드 ==//
    public void addShop(Shop shop) {
        this.shop = shop;
        shop.getShopImages().add(this);
    }
}
