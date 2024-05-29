package kyonggi.bookslyserver.global.common.uuid;

import kyonggi.bookslyserver.global.common.uuid.Uuid;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UuidRepository extends JpaRepository<Uuid, Long> {
}
