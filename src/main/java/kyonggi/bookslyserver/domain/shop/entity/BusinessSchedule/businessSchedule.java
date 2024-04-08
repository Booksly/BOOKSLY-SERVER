package kyonggi.bookslyserver.domain.shop.entity.BusinessSchedule;

import jakarta.persistence.*;
import kyonggi.bookslyserver.domain.shop.entity.Shop.Shop;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(of={""})
public class businessSchedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="businessDays_id")
    private BusinessDays businessdays;

    @ManyToOne
    @JoinColumn(name="id")
    private Shop shop;


}
