package kyonggi.bookslyserver.domain.event.repository;

import kyonggi.bookslyserver.domain.event.entity.timeEvent.EmployeeTimeEvent;
import kyonggi.bookslyserver.domain.event.entity.timeEvent.TimeEvent;
import kyonggi.bookslyserver.domain.shop.entity.Employee.Employee;
import kyonggi.bookslyserver.domain.shop.entity.Shop.Shop;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TimeEventRepository extends JpaRepository<TimeEvent, Long> {

    @Query("select t.shop from TimeEvent t join t.shop")
    List<Shop> findShopsByTimeEvent();
}
