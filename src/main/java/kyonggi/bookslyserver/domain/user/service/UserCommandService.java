package kyonggi.bookslyserver.domain.user.service;

import kyonggi.bookslyserver.domain.shop.entity.Shop.Shop;
import kyonggi.bookslyserver.domain.shop.service.ShopService;
import kyonggi.bookslyserver.domain.user.constant.Role;
import kyonggi.bookslyserver.domain.user.dto.request.CreateFavoriteShopRequestDto;
import kyonggi.bookslyserver.domain.user.dto.request.UpdateUserInfoRequestDto;
import kyonggi.bookslyserver.domain.user.dto.response.CreateFavoriteShopResponseDto;
import kyonggi.bookslyserver.domain.user.dto.response.DeleteFavoriteShopResponseDto;
import kyonggi.bookslyserver.domain.user.dto.response.UpdateUserInfoResponseDto;
import kyonggi.bookslyserver.domain.user.entity.FavoriteShop;
import kyonggi.bookslyserver.domain.user.entity.User;
import kyonggi.bookslyserver.domain.user.repository.FavoriteShopRepository;
import kyonggi.bookslyserver.domain.user.repository.UserRepository;
import kyonggi.bookslyserver.global.auth.principal.user.userInfo.OAuth2UserInfo;
import kyonggi.bookslyserver.global.error.ErrorCode;
import kyonggi.bookslyserver.global.error.exception.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserCommandService {

    private final UserRepository userRepository;
    private final UserQueryService userQueryService;
    private final ShopService shopService;
    private final FavoriteShopRepository favoriteShopRepository;


    private User createUser(String email, String loginId, String nickname, String profileImgUrl) {
        User createdUser = User.builder()
                .email(email)
                .loginId(loginId)
                .nickname(nickname)
                .profileImgUrl(profileImgUrl)
                .role(Role.ROLE_USER)
                .isVerified(false)
                .build();

        return userRepository.save(createdUser);
    }


    /**
     * 유저정보를 받아 회원가입한다.
     * 이미 가입되어 있다면 닉네임과 프로필이미지를 업데이트한다.
     *
     * @param userInfo 로그인을 요청하는 유저의 정보
     * @return 유저 객체
     */
    public User getOrCreateUser(OAuth2UserInfo userInfo) {
        String loginId = userInfo.getIdByProvider();
        String email = userInfo.getEmail();
        String nickname = userInfo.getNickname();
        String profileImgUrl = userInfo.getProfileImgUrl();

        return userRepository.findByLoginId(loginId)
                .map(user -> {
                    user.updateUserInfo(nickname, profileImgUrl);
                    return user;
                })
                .orElseGet(() -> createUser(email,loginId,nickname,profileImgUrl));
    }

    public UpdateUserInfoResponseDto updateUserInfo(Long userId, UpdateUserInfoRequestDto updateUserInfoRequestDto) {
        User user = userQueryService.findUser(userId);

        String phoneNumber = updateUserInfoRequestDto.phoneNumber();
        String nickname = updateUserInfoRequestDto.nickname();

        if (phoneNumber != null) user.updatePhoneNumber(phoneNumber);
        if (nickname != null) user.updateNickname(nickname);

        userRepository.save(user);
        return UpdateUserInfoResponseDto.of(user);
    }


    public CreateFavoriteShopResponseDto createFavoriteShop(Long userId, CreateFavoriteShopRequestDto createFavoriteShopRequestDto) {
        User user = userQueryService.findUser(userId);
        Shop shop = shopService.findShop(createFavoriteShopRequestDto.shopId());

        FavoriteShop favoriteShop = FavoriteShop.builder()
                .shop(shop)
                .user(user).build();

        FavoriteShop saved = favoriteShopRepository.save(favoriteShop);
        return CreateFavoriteShopResponseDto.of(saved);
    }

    public DeleteFavoriteShopResponseDto deleteFavoriteShop(Long favoriteShopId) {
        FavoriteShop favoriteShop = favoriteShopRepository.findById(favoriteShopId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.ENTITY_NOT_FOUND));

        favoriteShopRepository.deleteById(favoriteShopId);
        return DeleteFavoriteShopResponseDto.of(favoriteShop);
    }
}
