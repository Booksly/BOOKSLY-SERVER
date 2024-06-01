package kyonggi.bookslyserver.domain.review.repository;

import kyonggi.bookslyserver.domain.review.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    @Query("SELECT COUNT(r) FROM Review r WHERE r.employee.id = :employeeId")
    int countReviewsByEmployeeId(@Param("employeeId") Long employeeId);
}
