package kyonggi.bookslyserver.domain.shop.entity.Shop;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

@Embeddable
@Getter
@Setter
public class TimeUnit {
    private String hour;
    private String minute;

}
