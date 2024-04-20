package kyonggi.bookslyserver.global.auth.principal.shopOwner;

import kyonggi.bookslyserver.domain.user.entity.ShopOwner;
import kyonggi.bookslyserver.domain.user.entity.User;
import kyonggi.bookslyserver.domain.user.repository.ShopOwnerRepository;
import kyonggi.bookslyserver.domain.user.repository.UserRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

@Getter
public class OwnerPrincipalDetails implements UserDetails {

    private ShopOwner shopOwner;

    public OwnerPrincipalDetails(ShopOwner shopOwner) {

        this.shopOwner = shopOwner;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add((GrantedAuthority) () -> shopOwner.getUser().getRole().toString());
        return authorities;
    }

    @Override
    public String getPassword() {
        return shopOwner.getPassword();
    }

    @Override
    public String getUsername() {
        return shopOwner.getUser().getLoginId();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
