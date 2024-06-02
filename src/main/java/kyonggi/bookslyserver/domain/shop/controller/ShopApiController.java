package kyonggi.bookslyserver.domain.shop.controller;



import kyonggi.bookslyserver.domain.shop.dto.request.shop.ShopCreateRequestDto;
import kyonggi.bookslyserver.domain.shop.dto.request.shop.ShopUpdateRequestDto;
import kyonggi.bookslyserver.domain.shop.dto.response.shop.ShopCreateResponseDto;
import kyonggi.bookslyserver.domain.shop.dto.response.shop.*;
import kyonggi.bookslyserver.domain.shop.service.ShopService;
import kyonggi.bookslyserver.global.auth.principal.shopOwner.OwnerId;
import kyonggi.bookslyserver.global.common.SuccessResponse;
import kyonggi.bookslyserver.global.error.ErrorCode;
import kyonggi.bookslyserver.global.error.exception.InvalidValueException;
import lombok.RequiredArgsConstructor;
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
    @GetMapping("/api/shops/names")
    public ResponseEntity<SuccessResponse<?>> readShopNames(@OwnerId Long ownerId){
        return SuccessResponse.ok(shopService.readShopNames(ownerId));
    }

    //가게 상세 프로필 조회(유저)
    @GetMapping("/api/shops/{shopId}/profile")
    public ResponseEntity<SuccessResponse<?>> findShop(@PathVariable("shopId") Long shopId){
        return SuccessResponse.ok(shopService.getShopProfileDetails(shopId));
    }

    //가게 상세 프로필 조회(가게 주인)
    @GetMapping("/api/shops/{shopId}/profile/owner")
    public ResponseEntity<SuccessResponse<?>> readShop(@PathVariable("shopId") Long shopId){
        return SuccessResponse.ok(shopService.getShopProfileDetailsOwner(shopId));
    }


    //새로 입점한 가게 리스트 조회
    @GetMapping("/api/shops/new")
    public ResponseEntity<SuccessResponse<?>> readNewShops(@PageableDefault(size = 5, page = 0, sort = "createDate", direction = Sort.Direction.DESC) Pageable pageable){
                return SuccessResponse.ok(shopService.readNewShops(pageable));
    }
  
    //Top100 조회
    @GetMapping("/api/shops/top100")
    public ResponseEntity<SuccessResponse<?>> readTopShops(@PageableDefault(size = 10, page = 0, sort = "totalVisitors", direction = Sort.Direction.DESC) Pageable pageable){
        if(pageable.getPageNumber() > 9){
            throw new InvalidValueException(ErrorCode.PAGE_NUMBER_OVER);
        }
        return SuccessResponse.ok(shopService.readTopShops(pageable));
    }

    //가게 등록
    @PostMapping("/api/shops")
    public ResponseEntity<SuccessResponse<?>> createShop(@OwnerId Long ownerId, @RequestBody @Validated ShopCreateRequestDto requestDto){
        return SuccessResponse.ok(shopService.join(ownerId, requestDto));
    }

    //가게 수정
    @PutMapping("/api/shops/{shopId}")
    public ResponseEntity<SuccessResponse<?>> updateShop(@PathVariable("shopId") Long shopId, @RequestBody @Validated ShopUpdateRequestDto requestDto){
        return SuccessResponse.ok(shopService.update(shopId, requestDto));
    }

    //가게 삭제
    @DeleteMapping("/api/shops/{shopId}")
    public ResponseEntity<SuccessResponse<?>> deleteShop(@PathVariable("shopId") Long shopId){
        return SuccessResponse.ok(shopService.delete(shopId));
    }


}
