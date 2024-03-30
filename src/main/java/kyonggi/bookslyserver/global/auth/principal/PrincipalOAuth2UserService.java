package kyonggi.bookslyserver.global.auth.principal;

import kyonggi.bookslyserver.domain.user.entity.User;
import kyonggi.bookslyserver.domain.user.service.UserService;
import kyonggi.bookslyserver.global.auth.principal.userInfo.OAuth2UserInfo;
import kyonggi.bookslyserver.global.auth.principal.userInfo.OAuthAttributes;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
@Slf4j
public class PrincipalOAuth2UserService extends DefaultOAuth2UserService {

    private final UserService userService;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        OAuth2User oAuth2User = super.loadUser(userRequest);

        //registrationId = google
        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        //리소스 서버로부터 받아온 attributes로 userInfo 생성
        OAuth2UserInfo userInfo = OAuthAttributes.of(registrationId, oAuth2User.getAttributes());
        User user = userService.getOrCreateUser(userInfo);

        return new PrincipalDetails(user, oAuth2User.getAttributes());
    }
}
