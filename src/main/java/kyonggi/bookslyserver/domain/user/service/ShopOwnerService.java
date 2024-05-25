package kyonggi.bookslyserver.domain.user.service;

import kyonggi.bookslyserver.domain.shop.entity.Shop.Shop;
import kyonggi.bookslyserver.domain.shop.service.ShopService;
import kyonggi.bookslyserver.domain.user.dto.request.JoinOwnerRequestDto;
import kyonggi.bookslyserver.domain.user.dto.response.GetOwnerDetailInfoResponseDto;
import kyonggi.bookslyserver.domain.user.dto.response.GetOwnerLoginIdResponseDto;
import kyonggi.bookslyserver.domain.user.dto.response.GetOwnerProfileResponseDto;
import kyonggi.bookslyserver.domain.user.dto.response.JoinOwnerResponseDto;
import kyonggi.bookslyserver.domain.user.entity.ShopOwner;
import kyonggi.bookslyserver.domain.user.entity.User;
import kyonggi.bookslyserver.domain.user.repository.ShopOwnerRepository;
import kyonggi.bookslyserver.domain.user.repository.UserRepository;
import kyonggi.bookslyserver.global.error.ErrorCode;
import kyonggi.bookslyserver.global.error.exception.ConflictException;
import kyonggi.bookslyserver.global.error.exception.EntityNotFoundException;
import kyonggi.bookslyserver.global.error.exception.ForbiddenException;
import kyonggi.bookslyserver.global.error.exception.UnauthorizedException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static kyonggi.bookslyserver.global.error.ErrorCode.*;

@Service
@RequiredArgsConstructor
@Transactional
public class ShopOwnerService {

    private final ShopOwnerRepository shopOwnerRepository;
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final UserService userService;
    private final ShopService shopService;

    public JoinOwnerResponseDto join(JoinOwnerRequestDto joinOwnerRequestDto) {
        String loginId = joinOwnerRequestDto.loginId();
        if (userRepository.existsByLoginId(loginId)) {
            throw new ConflictException(ErrorCode.CONFLICT);
        }
        ShopOwner shopOwner = ShopOwner.createShopOwner(joinOwnerRequestDto,passwordEncoder);

        return JoinOwnerResponseDto.of(shopOwnerRepository.save(shopOwner));
    }

    public ShopOwner findShopOwnerByLoginId(String loginId) {
        User user = userService.findUserByLoginId(loginId);
        ShopOwner shopOwner = findShopOwnerByUserId(user.getId());
        shopOwner.addUser(user);

        return shopOwner;
    }

    public ShopOwner findShopOwnerByUserId(Long userId) {
        return shopOwnerRepository.findByUserId(userId).orElseThrow(()-> new EntityNotFoundException(MEMBER_NOT_FOUND));
    }

    public GetOwnerLoginIdResponseDto getLoginId(Long ownerId) {
        String loginId = shopOwnerRepository.findLoginId(ownerId).orElseThrow(()-> new EntityNotFoundException(MEMBER_NOT_FOUND));
        return GetOwnerLoginIdResponseDto.of(loginId);
    }

    public GetOwnerProfileResponseDto getOwnerProfile(Long shopId, Long ownerId) {
        Shop shop = shopService.findShop(ownerId, shopId);
        ShopOwner shopOwner = shopOwnerRepository.findByUserId(shopId).orElseThrow(() -> new EntityNotFoundException(MEMBER_NOT_FOUND));

        return GetOwnerProfileResponseDto.of(shop, shopOwner);
    }

    public GetOwnerDetailInfoResponseDto getOwnerDetailInfo(Long ownerId) {
        ShopOwner shopOwner = shopOwnerRepository.findById(ownerId).orElseThrow(() -> new EntityNotFoundException(MEMBER_NOT_FOUND));
        return GetOwnerDetailInfoResponseDto.of(shopOwner);
    }
}
