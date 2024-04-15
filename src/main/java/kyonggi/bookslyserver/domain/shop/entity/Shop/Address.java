package kyonggi.bookslyserver.domain.shop.entity.Shop;

import jakarta.persistence.*;
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
@ToString(of = {"first_address", "second_address", "third_address"})
public class Address extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String firstAddress;

    private String secondAddress;

    private String thirdAddress;

    @OneToMany(mappedBy = "address", cascade = CascadeType.PERSIST)
    private List<Shop> shops = new ArrayList<>();

/*    public Address(String firstAddress, String secondAddress, String thirdAddress){
        this.firstAddress=firstAddress;
        this.secondAddress=secondAddress;
        this.thirdAddress=thirdAddress;
    }*/
}
