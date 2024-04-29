package kyonggi.bookslyserver.domain.user.controller;


import kyonggi.bookslyserver.domain.user.dto.response.GetOwnerLoginIdResponseDto;
import kyonggi.bookslyserver.domain.user.service.ShopOwnerService;
import kyonggi.bookslyserver.global.auth.principal.shopOwner.OwnerId;
import kyonggi.bookslyserver.global.common.SuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/owner")
@RestController
@RequiredArgsConstructor
public class ShopOwnerController {

    private final ShopOwnerService shopOwnerService;

    @GetMapping("/details/login-id")
    public ResponseEntity<SuccessResponse<?>> getLoginId(@OwnerId Long ownerId) {
        GetOwnerLoginIdResponseDto getOwnerLoginIdResponseDto = shopOwnerService.getLoginId(ownerId);
        return SuccessResponse.ok(getOwnerLoginIdResponseDto);
    }
}
