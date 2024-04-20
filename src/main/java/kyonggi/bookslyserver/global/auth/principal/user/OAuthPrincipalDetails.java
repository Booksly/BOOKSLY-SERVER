package kyonggi.bookslyserver.global.auth.principal.user;

import kyonggi.bookslyserver.domain.user.entity.User;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

@Data
public class OAuthPrincipalDetails implements OAuth2User {

    private User user;
    private Map<String, Object> attributes;

    public OAuthPrincipalDetails(User user) {
        this.user = user;
    }

    public OAuthPrincipalDetails(User user, Map<String, Object> attributes) {
        this.user = user;
        this.attributes = attributes;
    }

    // 해당 User의 권한을 리턴하는 곳
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> collect = new ArrayList<>();
        collect.add((GrantedAuthority) () -> user.getRole().toString());
        return collect;
    }

    @Override
    public String getName() {
        return user.getNickname();
    }

    public String getLoginId() {
        return user.getLoginId();
    }
}
