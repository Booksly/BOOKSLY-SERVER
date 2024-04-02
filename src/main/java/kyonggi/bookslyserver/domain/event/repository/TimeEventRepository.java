package kyonggi.bookslyserver.domain.event.repository;

import kyonggi.bookslyserver.domain.event.entity.TimeEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TimeEventRepository extends JpaRepository<TimeEvent, Long> {
}
