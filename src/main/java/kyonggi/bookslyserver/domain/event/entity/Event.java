package kyonggi.bookslyserver.domain.event.entity;

import org.springframework.stereotype.Component;

@Component
public interface Event {
    String getTitle();

    int getDiscountRate();
}
