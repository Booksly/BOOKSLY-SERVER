package kyonggi.bookslyserver.domain.shop.entity;

import jakarta.persistence.*;
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
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="address_id")
    private Long id;

    private String first_address;

    private String second_address;

    private String third_address;

    @OneToMany(mappedBy = "address", cascade = CascadeType.PERSIST)
    private List<Shop> shops = new ArrayList<>();

}
