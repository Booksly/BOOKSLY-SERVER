package kyonggi.bookslyserver.domain.user.controller;


import kyonggi.bookslyserver.global.auth.principal.shopOwner.OwnerId;
import kyonggi.bookslyserver.global.common.SuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/owner")
@RestController
@RequiredArgsConstructor
public class ShopOwnerController {

    @PostMapping("/test")
    public ResponseEntity<SuccessResponse<?>> testShopOwnerCredential(@OwnerId Long ownerId) {
        return SuccessResponse.ok(ownerId);
    }
}
