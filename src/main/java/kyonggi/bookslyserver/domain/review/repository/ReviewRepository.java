package kyonggi.bookslyserver.domain.review.repository;

import kyonggi.bookslyserver.domain.review.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long> {
}
