package kyonggi.bookslyserver.domain.user.service;

import kyonggi.bookslyserver.domain.user.dto.response.*;
import kyonggi.bookslyserver.domain.user.entity.FavoriteShop;
import kyonggi.bookslyserver.domain.user.entity.User;
import kyonggi.bookslyserver.domain.user.repository.FavoriteShopRepository;
import kyonggi.bookslyserver.domain.user.repository.UserRepository;
import kyonggi.bookslyserver.global.error.ErrorCode;
import kyonggi.bookslyserver.global.error.exception.EntityNotFoundException;
import kyonggi.bookslyserver.global.error.exception.UnauthorizedException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class UserQueryService {

    private final UserRepository userRepository;
    private final FavoriteShopRepository favoriteShopRepository;

    public User findUserByLoginId(String loginId) {

        return userRepository.findByLoginId(loginId).orElseThrow(()-> new UnauthorizedException(ErrorCode.INVALID_ACCESS_TOKEN_VALUE));
    }

    public GetUserNicknameResponseDto getNickname(Long userId) {
        User user = findUser(userId);
        return GetUserNicknameResponseDto.of(user);
    }

    public GetUserProfileResponseDto getProfile(Long userId) {
        User user = findUser(userId);
        return GetUserProfileResponseDto.of(user);
    }

    public GetUserDetailInfoResponseDto getDetailInfo(Long userId) {
        User user = findUser(userId);
        return GetUserDetailInfoResponseDto.of(user);
    }

    public User findUser(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException(ErrorCode.ENTITY_NOT_FOUND));
        return user;
    }

    public GetFavoriteShopsResponseDto getFavoriteShops(Long userId) {
        List<FavoriteShop> favoriteShops = favoriteShopRepository.findByUserId(userId);
        return GetFavoriteShopsResponseDto.of(favoriteShops);
    }
}
