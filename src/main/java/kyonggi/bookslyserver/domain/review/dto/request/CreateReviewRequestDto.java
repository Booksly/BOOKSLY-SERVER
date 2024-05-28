package kyonggi.bookslyserver.domain.review.dto.request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateReviewRequestDto {

    @NotNull
    private Long reservationId;

    @NotNull @DecimalMin(value = "0.5",message = "0.5점 이상이어야 합니다.") @DecimalMax(value = "5",message = "5점 이하여야 햡니다.")
    private Float rating;

    @NotBlank
    private String content;

    private List<MultipartFile> reviewPictures;

}