package kyonggi.bookslyserver.domain.shop.controller;



import kyonggi.bookslyserver.domain.shop.dto.request.shop.ShopCreateRequestDto;
import kyonggi.bookslyserver.domain.shop.dto.request.shop.ShopUpdateRequestDto;
import kyonggi.bookslyserver.domain.shop.dto.response.shop.ShopCreateResponseDto;
import kyonggi.bookslyserver.domain.shop.dto.response.shop.ShopRegisterDto;
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
    @GetMapping("/api/shops/shopNames")
    public ResponseEntity<SuccessResponse<?>> readShopNames(@OwnerId Long ownerId){
        return SuccessResponse.ok(shopService.readShopNames(ownerId));
    }

    //가게 상세 프로필 조회(유저)
    @GetMapping("/api/shops/{shopId}/profile/user")
    public ResponseEntity<SuccessResponse<?>> findShop(@PathVariable("shopId") Long shopId){
        return SuccessResponse.ok(shopService.getShopProfileDetails(shopId));
    }


    //가게 상세 프로필 조회(가게 주인)
    @GetMapping("/api/shops/{shopId}/profile/owner")
    public ResponseEntity<SuccessResponse<?>> readShop(@PathVariable("shopId") Long id){
        ShopOwnerDetailReadOneDto result = shopService.getShopProfileDetailsOwner(id);
        return SuccessResponse.ok(result);
    }

    //가게 메인 페이지 조회(가게 주인)
    @GetMapping("/api/shops/{shopId}/mainProfile/owner")
    public ResponseEntity<SuccessResponse<?>> readMain(@PathVariable("shopId") Long id){
        ShopOwnerMainReadOneDto result = shopService.readMain(id);
        return SuccessResponse.ok(result);
    }



    //새로 입점한 가게 리스트 조회
    @GetMapping("/api/shops/newshops")
    public ResponseEntity<SuccessResponse<?>> readNewShops(@PageableDefault(size = 5, page = 0, sort = "createdAt", direction = Sort.Direction.ASC) Pageable pageable){
        List<NewShopFilterDto> result = shopService.readNewShops(pageable);
        return SuccessResponse.ok(result);
    }
  
    //Top100 조회
    @GetMapping("/api/shops/top100")
    public ResponseEntity<SuccessResponse<?>> readTopShops(@PageableDefault(size = 10, page = 0, sort = "totalVisitors", direction = Sort.Direction.DESC) Pageable pageable){
        if(pageable.getPageNumber() > 9){
            throw new InvalidValueException(ErrorCode.PAGE_NUMBER_OVER);
        }
        List<ShopFilterDto> result = shopService.readTopShops(pageable);
        return SuccessResponse.ok(result);
    }

    //가게 등록
    @PostMapping("/api/shops")
    public ResponseEntity<SuccessResponse<?>> createShop(@OwnerId Long ownerId, @RequestBody @Validated ShopCreateRequestDto requestDto){
        return SuccessResponse.ok(shopService.join(ownerId, requestDto));
    }

    //가게 수정
    @PutMapping("/api/shops/{shopId}")
    public ResponseEntity<SuccessResponse<?>> updateShop(@PathVariable("shopId") Long id, @RequestBody @Validated ShopUpdateRequestDto requestDto){
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
