package kyonggi.bookslyserver.global.auth.principal.userInfo;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;

public enum OAuthAttributes {

    GOOGLE("google",attributes-> new OAuth2UserInfo(
            "google_"+ attributes.get("sub").toString(),
            attributes.get("name").toString(),
            attributes.get("picture").toString(),
            attributes.get("email").toString()
    )),

    NAVER("naver", attributes -> {

        Map<String, Object> response = (Map<String, Object>) attributes.get("response");
        return new OAuth2UserInfo(
                "naver_"+ response.get("id").toString(),
                response.get("name").toString(),
                response.get("profile_image").toString(),
                response.get("email").toString()
        );
    }),


//    KAKAO("kakao", attributes -> {
//        Map<String, Object> account = (Map<String, Object>) attributes.get("kakao_account");
//        Map<String, Object> profile = (Map<String, Object>) attributes.get("properties");
//        return new OAuth2UserInfo(
//                attributes.get("id").toString(),
//                profile.get("nickname").toString(),
//                profile.get("profile_image").toString(),
//                account.get("email").toString()
//        );
//    })
    ;
    private final String providerId;
    private final Function<Map<String, Object>, OAuth2UserInfo> of;

    OAuthAttributes(String providerId, Function<Map<String, Object>, OAuth2UserInfo> of) {
        this.providerId = providerId;
        this.of = of;
    }

    public static OAuth2UserInfo of(String providerId, Map<String, Object> attributes) {
        return Arrays.stream(values())
                .filter(oauthProvider -> oauthProvider.providerId.equals(providerId))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new)
                .of.apply(attributes);
    }
}
