package kyonggi.bookslyserver.domain.shop.controller;



import kyonggi.bookslyserver.domain.shop.dto.request.shop.ShopCreateRequestDto;
import kyonggi.bookslyserver.domain.shop.dto.response.shop.ShopCreateResponseDto;
import kyonggi.bookslyserver.domain.shop.dto.response.shop.ShopRegisterDto;
import kyonggi.bookslyserver.domain.shop.dto.response.shop.ShopUserReadOneDto;
import kyonggi.bookslyserver.domain.shop.dto.response.shop.*;
import kyonggi.bookslyserver.domain.shop.service.ShopService;
import kyonggi.bookslyserver.global.auth.principal.shopOwner.OwnerId;
import kyonggi.bookslyserver.global.auth.principal.user.UserId;
import kyonggi.bookslyserver.global.common.SuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
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

    //가게 상세 프로필 조회(유저)
    @GetMapping("/api/shops/{shopId}/profile/user")
    public ResponseEntity<SuccessResponse<?>> findShop(@PathVariable("shopId") Long id){
        ShopUserReadOneDto result = shopService.findOne(id);
        return SuccessResponse.ok(result);
    }


    //가게 상세 프로필 조회(가게 주인)
    @GetMapping("/api/shops/{shopId}/profile/owner")
    public ResponseEntity<SuccessResponse<?>> readShop(@PathVariable("shopId") Long id){
        ShopOwnerDetailReadOneDto result = shopService.readOne(id);
        return SuccessResponse.ok(result);
    }

    //Top100 조회
    @GetMapping("/api/shops/top100")
    public ResponseEntity<SuccessResponse<?>> readTopShops(@PageableDefault(size = 10, page = 10, sort = "totalVisitors", direction = Sort.Direction.DESC) Pageable pageable){
        List<ShopFilterDto> result = shopService.readTopShops(pageable);
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
