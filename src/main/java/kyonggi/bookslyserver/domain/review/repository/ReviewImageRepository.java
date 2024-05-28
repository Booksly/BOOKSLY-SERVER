package kyonggi.bookslyserver.domain.review.repository;

import kyonggi.bookslyserver.domain.review.entity.ReviewImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewImageRepository extends JpaRepository<ReviewImage, Long> {
}
