package kyonggi.bookslyserver.domain.review.dto.response;

import kyonggi.bookslyserver.domain.review.entity.Review;
import lombok.Builder;

@Builder
public record CreateReviewResponseDto(
        Long reviewId
) {
    public static CreateReviewResponseDto of(Review review) {
        return CreateReviewResponseDto.builder().reviewId(review.getId()).build();
    }
}
