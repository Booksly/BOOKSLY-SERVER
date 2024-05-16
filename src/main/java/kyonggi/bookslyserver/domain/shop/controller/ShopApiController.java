package kyonggi.bookslyserver.domain.shop.controller;



import kyonggi.bookslyserver.domain.shop.dto.request.ShopCreateRequestDto;
import kyonggi.bookslyserver.domain.shop.dto.response.shop.*;
import kyonggi.bookslyserver.domain.shop.service.ShopService;
import kyonggi.bookslyserver.global.auth.principal.shopOwner.OwnerId;
import kyonggi.bookslyserver.global.auth.principal.user.UserId;
import kyonggi.bookslyserver.global.common.SuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ShopApiController {

    private final ShopService shopService;

    //가게 이름 조회(가게 주인)
    @GetMapping("/api/shops/shopnames")
    public ResponseEntity<SuccessResponse<?>> readShopNames(@OwnerId Long id){
        List<ReadShopNamesDto> result = shopService.readShopNames(id);
        return SuccessResponse.ok(result);
    }


    //가게 상세 프로필 조회(가게 주인)
    @GetMapping("/api/shops/{shopId}/profile/owner")
    public ResponseEntity<SuccessResponse<?>> readShop(@PathVariable("shopId") Long id){
        ShopOwnerDetailReadOneDto result = shopService.readOne(id);
        return SuccessResponse.ok(result);
    }


    //가게 등록
    @PostMapping("/api/shops")
    public ResponseEntity<SuccessResponse<?>> createShop(@OwnerId Long id, @RequestBody @Validated ShopCreateRequestDto requestDto){
        ShopRegisterDto result = shopService.join(id, requestDto);
        return SuccessResponse.ok(result);
    }

    //가게 수정
    @PutMapping("/api/shops/{shopId}")
    public ResponseEntity<SuccessResponse<?>> updateShop(@PathVariable("shopId") Long id, @RequestBody @Validated ShopCreateRequestDto requestDto){
        ShopCreateResponseDto result = shopService.update(id, requestDto);
        return SuccessResponse.ok(result);
    }

    //가게 삭제
    @DeleteMapping("/api/shops/{shopId}")
    public ResponseEntity<SuccessResponse<?>> deleteShop(@PathVariable("shopId") Long id){
        ShopDeleteResponseDto result = shopService.delete(id);
        return SuccessResponse.ok(result);
    }

}
