package kyonggi.bookslyserver.domain.user.service;

import kyonggi.bookslyserver.domain.user.dto.request.JoinOwnerRequestDto;
import kyonggi.bookslyserver.domain.user.dto.response.GetOwnerLoginIdResponseDto;
import kyonggi.bookslyserver.domain.user.dto.response.JoinOwnerResponseDto;
import kyonggi.bookslyserver.domain.user.entity.ShopOwner;
import kyonggi.bookslyserver.domain.user.entity.User;
import kyonggi.bookslyserver.domain.user.repository.ShopOwnerRepository;
import kyonggi.bookslyserver.domain.user.repository.UserRepository;
import kyonggi.bookslyserver.global.error.ErrorCode;
import kyonggi.bookslyserver.global.error.exception.ConflictException;
import kyonggi.bookslyserver.global.error.exception.EntityNotFoundException;
import kyonggi.bookslyserver.global.error.exception.UnauthorizedException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class ShopOwnerService {

    private final ShopOwnerRepository shopOwnerRepository;
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final UserService userService;

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
        return shopOwnerRepository.findByUserId(userId);
    }

    public GetOwnerLoginIdResponseDto getLoginId(Long ownerId) {
        String loginId = shopOwnerRepository.findLoginId(ownerId).orElseThrow(()-> new EntityNotFoundException(ErrorCode.ENTITY_NOT_FOUND));
        return GetOwnerLoginIdResponseDto.of(loginId);
    }
}
