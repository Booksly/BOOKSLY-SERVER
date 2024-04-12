package kyonggi.bookslyserver.domain.user.service;

import kyonggi.bookslyserver.domain.user.dto.request.JoinOwnerRequestDto;
import kyonggi.bookslyserver.domain.user.dto.response.JoinOwnerResponseDto;
import kyonggi.bookslyserver.domain.user.entity.ShopOwner;
import kyonggi.bookslyserver.domain.user.repository.ShopOwnerRepository;
import kyonggi.bookslyserver.domain.user.repository.UserRepository;
import kyonggi.bookslyserver.global.error.ErrorCode;
import kyonggi.bookslyserver.global.error.exception.ConflictException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ShopOwnerService {

    private final ShopOwnerRepository shopOwnerRepository;
    private final UserRepository userRepository;

    public JoinOwnerResponseDto join(JoinOwnerRequestDto joinOwnerRequestDto) {
        String loginId = joinOwnerRequestDto.loginId();
        if (userRepository.existsByLoginId(loginId)) {
            throw new ConflictException(ErrorCode.CONFLICT);
        }
        ShopOwner shopOwner = ShopOwner.createShopOwner(joinOwnerRequestDto);

        return JoinOwnerResponseDto.of(shopOwnerRepository.save(shopOwner));
    }
}
