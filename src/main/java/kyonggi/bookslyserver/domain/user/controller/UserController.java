package kyonggi.bookslyserver.domain.user.controller;

import jakarta.validation.Valid;
import kyonggi.bookslyserver.domain.user.dto.request.CreateFavoriteShopRequestDto;
import kyonggi.bookslyserver.domain.user.dto.request.UpdateUserInfoRequestDto;
import kyonggi.bookslyserver.domain.user.dto.response.*;
import kyonggi.bookslyserver.domain.user.service.UserCommandService;
import kyonggi.bookslyserver.domain.user.service.UserQueryService;
import kyonggi.bookslyserver.global.auth.principal.user.UserId;
import kyonggi.bookslyserver.global.common.SuccessResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/api/users")
@RestController
@Slf4j
public class UserController {

    private final UserQueryService userQueryService;
    private final UserCommandService userCommandService;


    @GetMapping("/test")
    public ResponseEntity<SuccessResponse<?>> test() {
        return SuccessResponse.ok("임시 리다이렉트 페이지");
    }

    @GetMapping("/details")
    public ResponseEntity<SuccessResponse<?>> getUserDetailInfo(@UserId Long userId) {
        GetUserDetailInfoResponseDto getUserDetailInfoResponseDto = userQueryService.getDetailInfo(userId);
        return SuccessResponse.ok(getUserDetailInfoResponseDto);
    }

    @GetMapping("/details/nickname")
    public ResponseEntity<SuccessResponse<?>> getNickname(@UserId Long userId) {
        GetUserNicknameResponseDto getUserNicknameResponseDto = userQueryService.getNickname(userId);
        return SuccessResponse.ok(getUserNicknameResponseDto);
    }

    @PatchMapping("/details")
    public ResponseEntity<SuccessResponse<?>> updateUserInfo(@UserId Long userId, @RequestBody @Valid UpdateUserInfoRequestDto updateUserInfoRequestDto) {
        UpdateUserInfoResponseDto updateUserInfoResponseDto = userCommandService.updateUserInfo(userId, updateUserInfoRequestDto);
        return SuccessResponse.ok(updateUserInfoResponseDto);
    }

    @GetMapping("/profile")
    public ResponseEntity<SuccessResponse<?>> getProfile(@UserId Long userId) {
        GetUserProfileResponseDto getProfileResponseDto = userQueryService.getProfile(userId);
        return SuccessResponse.ok(getProfileResponseDto);
    }

    @PostMapping("/favorite-shops")
    public ResponseEntity<SuccessResponse<?>> createFavoriteShop(@UserId Long userId, @RequestBody CreateFavoriteShopRequestDto createFavoriteShopRequestDto) {
        CreateFavoriteShopResponseDto createFavoriteShopResponseDto = userCommandService.createFavoriteShop(userId, createFavoriteShopRequestDto);
        return SuccessResponse.created(createFavoriteShopResponseDto);
    }

    @DeleteMapping("/favorite-shops/{favoriteShopId}")
    public ResponseEntity<SuccessResponse<?>> deleteFavoriteShop(@PathVariable("favoriteShopId") Long favoriteShopId) {
        DeleteFavoriteShopResponseDto deleteFavoriteShopResponseDto = userCommandService.deleteFavoriteShop(favoriteShopId);
        return SuccessResponse.ok(deleteFavoriteShopResponseDto);
    }
}
