package kyonggi.bookslyserver.domain.shop.controller;


import kyonggi.bookslyserver.domain.shop.dto.request.ShopCreateRequestDto;
import kyonggi.bookslyserver.domain.shop.dto.response.ShopCreateResponseDto;
import kyonggi.bookslyserver.domain.shop.service.ShopService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ShopApiController {

    private final ShopService shopService;

    //가게 등록
    @PostMapping("/api/shop/create")
    public ShopCreateResponseDto createShop(@RequestBody @Validated ShopCreateRequestDto requestDto){
        ShopCreateResponseDto result = shopService.join(requestDto);
        return result;
    }

}
