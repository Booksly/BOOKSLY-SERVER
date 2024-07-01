package kyonggi.bookslyserver.domain.shop.dto.request.shop;

import jakarta.validation.constraints.NotNull;
import kyonggi.bookslyserver.domain.shop.entity.Shop.TimeUnit;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class ShopCreateRequestDto {

    @NotNull private String name;

    @NotNull private Long categoryId;

    @NotNull private String phoneNumber;

    @NotNull private String businessNumber;

    @NotNull private String firstAddress;

    @NotNull private String secondAddress;

    @NotNull private String thirdAddress;

    @NotNull private String detailAddress;

    @NotNull private Long zipCode;

    @NotNull private String streetAddress;

    private String snsUrl;

    private String introduction;

    @NotNull private List<BusinessScheduleRequestDto> businessScheduleList;

    @NotNull private MultipartFile shopImage;

    private TimeUnit timeUnit;


}
