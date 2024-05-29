package kyonggi.bookslyserver.domain.review.service;

import kyonggi.bookslyserver.domain.reservation.entity.Reservation;
import kyonggi.bookslyserver.domain.reservation.entity.ReservationSchedule;
import kyonggi.bookslyserver.domain.reservation.repository.ReservationRepository;
import kyonggi.bookslyserver.domain.review.dto.request.CreateReviewRequestDto;
import kyonggi.bookslyserver.domain.review.dto.response.CreateReviewResponseDto;
import kyonggi.bookslyserver.domain.review.dto.response.GetUserReviewResponseDto;
import kyonggi.bookslyserver.domain.review.entity.Review;
import kyonggi.bookslyserver.domain.review.entity.ReviewImage;
import kyonggi.bookslyserver.domain.review.repository.ReviewImageRepository;
import kyonggi.bookslyserver.domain.review.repository.ReviewRepository;
import kyonggi.bookslyserver.domain.user.service.UserQueryService;
import kyonggi.bookslyserver.global.aws.s3.AmazonS3Manager;
import kyonggi.bookslyserver.global.common.uuid.Uuid;
import kyonggi.bookslyserver.global.common.uuid.UuidRepository;
import kyonggi.bookslyserver.global.common.uuid.UuidService;
import kyonggi.bookslyserver.global.error.ErrorCode;
import kyonggi.bookslyserver.global.error.exception.ConflictException;
import kyonggi.bookslyserver.global.error.exception.EntityNotFoundException;
import kyonggi.bookslyserver.global.error.exception.ForbiddenException;
import kyonggi.bookslyserver.global.error.exception.InvalidValueException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static kyonggi.bookslyserver.global.error.ErrorCode.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReviewService {

    private static final long MAX_FILE_SIZE = 2 * 1024 * 1024; // 2MB
    private static final int MAX_PICTURE_COUNT = 4;

    private final UserQueryService userQueryService;
    private final ReservationRepository reservationRepository;
    private final ReviewRepository reviewRepository;
    private final AmazonS3Manager amazonS3Manager;
    private final UuidService uuidService;
    private final ReviewImageRepository reviewImageRepository;

    private void validateReviewPictures(List<MultipartFile> reviewPictures) {
        if (reviewPictures == null) {
            return;
        }

        if (reviewPictures.size() > MAX_PICTURE_COUNT) {
            throw new InvalidValueException(MAX_PICTURE_SIZE_OVER);
        }

        Set<String> pictureNames = new HashSet<>();
        for (MultipartFile picture : reviewPictures) {
            if (picture.getSize() > MAX_FILE_SIZE) {
                throw new InvalidValueException(MAX_FILE_SIZE_OVER);
            }

            if (!pictureNames.add(picture.getOriginalFilename())) {
                throw new InvalidValueException(FILE_NAME_DUPLICATE);
            }
        }
    }


    private void uploadFileToS3(CreateReviewRequestDto createReviewRequestDto, Review savedReview, Uuid savedUuid) {
        createReviewRequestDto.getReviewPictures().forEach(picture-> {
            String pictureUrl = amazonS3Manager.uploadFile(
                    amazonS3Manager.generateReviewKeyName(savedUuid, picture.getOriginalFilename()), picture);

            ReviewImage reviewImage = ReviewImage.builder()
                    .review(savedReview)
                    .reviewImgUrl(pictureUrl)
                    .build();
            reviewImageRepository.save(reviewImage);
        });
    }


    public CreateReviewResponseDto createReview(Long userId, CreateReviewRequestDto createReviewRequestDto) {
        validateReviewPictures(createReviewRequestDto.getReviewPictures());

        Reservation reservation = reservationRepository.findById(createReviewRequestDto.getReservationId()).orElseThrow(() -> new EntityNotFoundException(RESERVATION_NOT_FOUND));
        if (reservation.getReview() != null) throw new ConflictException(ErrorCode.REVIEW_ALREADY_EXISTS);

        ReservationSchedule reservationSchedule = reservation.getReservationSchedule();
        Review review = Review.builder()
                .shop(reservationSchedule.getShop())
                .employee(reservationSchedule.getEmployee())
                .user(userQueryService.findUser(userId))
                .content(createReviewRequestDto.getContent())
                .rating(createReviewRequestDto.getRating()).build();

        Review savedReview = reviewRepository.save(review);
        reservation.addReview(savedReview);

        Uuid savedUuid = uuidService.createUuid();

        if (createReviewRequestDto.getReviewPictures() != null && !createReviewRequestDto.getReviewPictures().isEmpty())
            uploadFileToS3(createReviewRequestDto, savedReview, savedUuid);

        return CreateReviewResponseDto.of(savedReview);
    }

    public GetUserReviewResponseDto getUserReview(Long userId, Long reviewId) {
        Review review = reviewRepository.findById(reviewId).orElseThrow(() -> new EntityNotFoundException(REVIEW_NOT_FOUND));
        if (review.getUser().getId() != userId) {
            throw new ForbiddenException();
        }
        return GetUserReviewResponseDto.of(review);
    }

    public void deleteUserReview(Long userId, Long reviewId) {
        Review review = reviewRepository.findById(reviewId).orElseThrow(() -> new EntityNotFoundException(REVIEW_NOT_FOUND));
        if (review.getUser().getId() != userId) throw new ForbiddenException();

        if (review.getReviewImages() != null) {
            review.getReviewImages().stream().
                    forEach(reviewImage -> amazonS3Manager.deleteFile(amazonS3Manager.extractKeyNameFromUrl(reviewImage.getReviewImgUrl())));
        }

        review.getReservation().deleteReview(review);
        reviewRepository.deleteById(reviewId);
    }
}
