package kyonggi.bookslyserver.domain.user.controller;

import kyonggi.bookslyserver.domain.user.dto.request.JoinOwnerRequestDto;
import kyonggi.bookslyserver.domain.user.dto.response.JoinOwnerResponseDto;
import kyonggi.bookslyserver.domain.user.service.ShopOwnerService;
import kyonggi.bookslyserver.global.common.SuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("api/owner")
@RestController
@RequiredArgsConstructor
public class ShopOwnerController {

    private final ShopOwnerService shopOwnerService;

    @PostMapping("/")
    public ResponseEntity<SuccessResponse<?>> join(@RequestBody JoinOwnerRequestDto joinOwnerRequestDto) {
        JoinOwnerResponseDto joinOwnerResponseDto = shopOwnerService.join(joinOwnerRequestDto);
        return SuccessResponse.created(joinOwnerResponseDto);
    }
}
