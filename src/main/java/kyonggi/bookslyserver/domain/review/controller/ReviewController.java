package kyonggi.bookslyserver.domain.review.controller;

import jakarta.validation.Valid;
import kyonggi.bookslyserver.domain.review.dto.request.CreateReviewRequestDto;
import kyonggi.bookslyserver.domain.review.dto.response.CreateReviewResponseDto;
import kyonggi.bookslyserver.domain.review.dto.response.GetUserReviewResponseDto;
import kyonggi.bookslyserver.domain.review.service.ReviewService;
import kyonggi.bookslyserver.global.auth.principal.user.UserId;
import kyonggi.bookslyserver.global.common.SuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/api/shops")
@RestController
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping(value = "/reviews",consumes = "multipart/form-data")
    public ResponseEntity<SuccessResponse<?>> createReview(@UserId Long userId, @ModelAttribute @Valid CreateReviewRequestDto createReviewRequestDto) {
        CreateReviewResponseDto createReviewResponseDto = reviewService.createReview(userId, createReviewRequestDto);
        return SuccessResponse.created(createReviewResponseDto);
    }

    @GetMapping("/reviews/{reviewId}")
    public ResponseEntity<SuccessResponse<?>> getUserReview(@UserId Long userId, @PathVariable("reviewId")Long reviewId) {
        GetUserReviewResponseDto getUserReviewResponseDto = reviewService.getUserReview(userId, reviewId);
        return SuccessResponse.created(getUserReviewResponseDto);
    }
}
