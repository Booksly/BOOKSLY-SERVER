package kyonggi.bookslyserver.domain.review.dto.response;


import kyonggi.bookslyserver.domain.review.entity.Review;
import lombok.Builder;

import java.util.List;
import java.util.stream.Collectors;

@Builder
public record GetUserReviewResponseDto(
        String content,
        float rating,
        List<String> reviewImages
) {
    public static GetUserReviewResponseDto of(Review review) {
        return GetUserReviewResponseDto.builder()
                .content(review.getContent())
                .rating(review.getRating())
                .reviewImages(review.getReviewImages().stream().map(reviewImage -> reviewImage.getReviewImgUrl()).collect(Collectors.toList()))
                .build();
    }
}
