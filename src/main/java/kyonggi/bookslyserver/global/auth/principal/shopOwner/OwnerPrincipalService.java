package kyonggi.bookslyserver.global.auth.principal.shopOwner;

import kyonggi.bookslyserver.domain.user.entity.ShopOwner;
import kyonggi.bookslyserver.domain.user.entity.User;
import kyonggi.bookslyserver.domain.user.repository.ShopOwnerRepository;
import kyonggi.bookslyserver.domain.user.repository.UserRepository;
import kyonggi.bookslyserver.domain.user.service.ShopOwnerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class OwnerPrincipalService implements UserDetailsService {

    private final ShopOwnerService shopOwnerService;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        ShopOwner owner = shopOwnerService.findShopOwnerByLoginId(username);

        return new OwnerPrincipalDetails(owner);
    }
}
