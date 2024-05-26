package kyonggi.bookslyserver.domain.user.service;

import kyonggi.bookslyserver.domain.shop.entity.Shop.Shop;
import kyonggi.bookslyserver.domain.shop.repository.ShopRepository;
import kyonggi.bookslyserver.domain.shop.service.ShopService;
import kyonggi.bookslyserver.domain.user.constant.Role;
import kyonggi.bookslyserver.domain.user.dto.request.CreateFavoriteShopRequestDto;
import kyonggi.bookslyserver.domain.user.dto.request.UpdateUserInfoRequestDto;
import kyonggi.bookslyserver.domain.user.dto.response.*;
import kyonggi.bookslyserver.domain.user.entity.FavoriteShop;
import kyonggi.bookslyserver.domain.user.entity.User;
import kyonggi.bookslyserver.domain.user.repository.FavoriteShopRepository;
import kyonggi.bookslyserver.domain.user.repository.UserRepository;
import kyonggi.bookslyserver.global.auth.principal.user.userInfo.OAuth2UserInfo;
import kyonggi.bookslyserver.global.error.ErrorCode;
import kyonggi.bookslyserver.global.error.exception.EntityNotFoundException;
import kyonggi.bookslyserver.global.error.exception.UnauthorizedException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final ShopService shopService;
    private final FavoriteShopRepository favoriteShopRepository;

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

    @Transactional(readOnly = true)
    public User findUserByLoginId(String loginId) {

        return userRepository.findByLoginId(loginId).orElseThrow(()-> new UnauthorizedException(ErrorCode.INVALID_ACCESS_TOKEN_VALUE));
    }

    public GetUserNicknameResponseDto getNickname(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.ENTITY_NOT_FOUND));
        return GetUserNicknameResponseDto.of(user);
    }

    public GetUserProfileResponseDto getProfile(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.ENTITY_NOT_FOUND));
        return GetUserProfileResponseDto.of(user);
    }

    public GetUserDetailInfoResponseDto getDetailInfo(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.ENTITY_NOT_FOUND));
        return GetUserDetailInfoResponseDto.of(user);
    }

    public UpdateUserInfoResponseDto updateUserInfo(Long userId, UpdateUserInfoRequestDto updateUserInfoRequestDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.ENTITY_NOT_FOUND));
        
        String phoneNumber = updateUserInfoRequestDto.phoneNumber();
        String nickname = updateUserInfoRequestDto.nickname();
        
        if (phoneNumber != null) user.updatePhoneNumber(phoneNumber);
        if (nickname != null) user.updateNickname(nickname);

        userRepository.save(user);

        return UpdateUserInfoResponseDto.of(user);
    }

    public CreateFavoriteShopResponseDto createFavoriteShop(Long userId, CreateFavoriteShopRequestDto createFavoriteShopRequestDto) {
        User user = userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException(ErrorCode.ENTITY_NOT_FOUND));
        Shop shop = shopService.findShop(createFavoriteShopRequestDto.shopId());

        FavoriteShop favoriteShop = FavoriteShop.builder()
                .shop(shop)
                .user(user).build();

        FavoriteShop saved = favoriteShopRepository.save(favoriteShop);
        return CreateFavoriteShopResponseDto.of(saved);
    }
}
