package kyonggi.bookslyserver.domain.user.controller;


import kyonggi.bookslyserver.domain.user.dto.response.GetOwnerLoginIdResponseDto;
import kyonggi.bookslyserver.domain.user.dto.response.GetOwnerProfileResponseDto;
import kyonggi.bookslyserver.domain.user.service.ShopOwnerService;
import kyonggi.bookslyserver.global.auth.principal.shopOwner.OwnerId;
import kyonggi.bookslyserver.global.common.SuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/owners")
@RestController
@RequiredArgsConstructor
public class ShopOwnerController {

    private final ShopOwnerService shopOwnerService;

    @GetMapping("/details/login-id")
    public ResponseEntity<SuccessResponse<?>> getLoginId(@OwnerId Long ownerId) {
        GetOwnerLoginIdResponseDto getOwnerLoginIdResponseDto = shopOwnerService.getLoginId(ownerId);
        return SuccessResponse.ok(getOwnerLoginIdResponseDto);
    }

    @GetMapping("/shops/{shopId}/profile")
    public ResponseEntity<SuccessResponse<?>> getOwnerProfileByShop(@PathVariable("shopId") Long shopId, @OwnerId Long ownerId) {
        GetOwnerProfileResponseDto getOwnerProfileResponseDto = shopOwnerService.getOwnerProfile(shopId, ownerId);
        return SuccessResponse.ok(getOwnerProfileResponseDto);
    }
}
