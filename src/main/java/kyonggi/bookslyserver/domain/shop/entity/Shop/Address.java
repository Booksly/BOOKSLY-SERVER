package kyonggi.bookslyserver.domain.shop.entity.Shop;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@ToString(of = {"first_address", "second_address", "third_address"})
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="address_id")
    private Long id;

    private String first_address;

    private String second_address;

    private String third_address;


}
