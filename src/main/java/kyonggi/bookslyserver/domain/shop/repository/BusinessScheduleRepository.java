package kyonggi.bookslyserver.domain.shop.repository;

import kyonggi.bookslyserver.domain.shop.entity.BusinessSchedule.BusinessSchedule;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BusinessScheduleRepository extends JpaRepository<BusinessSchedule, Long> {
}
