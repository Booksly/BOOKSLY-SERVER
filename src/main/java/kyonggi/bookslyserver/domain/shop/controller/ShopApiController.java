package kyonggi.bookslyserver.domain.shop.controller;


import kyonggi.bookslyserver.domain.shop.dto.request.ShopCreateRequestDto;
import kyonggi.bookslyserver.domain.shop.dto.response.ShopCreateResponseDto;
import kyonggi.bookslyserver.domain.shop.entity.Shop.Shop;
import kyonggi.bookslyserver.domain.shop.service.ShopService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class ShopApiController {

    private final ShopService shopService;

    //가게 등록
    @PostMapping("/api/shop/create/{shopOwnerId}")
    public ShopCreateResponseDto createShop(@PathVariable("shopOwnerId") Long id, @RequestBody @Validated ShopCreateRequestDto requestDto){
        ShopCreateResponseDto result = shopService.join(id, requestDto);
        return result;
    }

    //가게 수정
    @PutMapping("/api/shop/{id}/update")
    public ShopCreateResponseDto updateShop(@PathVariable("id") Long id, @RequestBody @Validated ShopCreateRequestDto requestDto){
        ShopCreateResponseDto result = shopService.update(id, requestDto);
        return result;
    }

    //가게 삭제
    @DeleteMapping("/api/shop/{id}/delete")
    public void deleteShop(@PathVariable("id") Long id){
        shopService.delete(id);
    }




}
