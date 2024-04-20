package kyonggi.bookslyserver.global.auth.principal.user.userInfo;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class OAuth2UserInfo {
    private final String idByProvider;
    private final String nickname;
    private final String profileImgUrl;
    private final String email;
}
