package kyonggi.bookslyserver.global.auth.handelr;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kyonggi.bookslyserver.global.auth.jwt.JwtProvider;
import kyonggi.bookslyserver.global.auth.principal.PrincipalDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;


import java.io.IOException;


@Component
@RequiredArgsConstructor
@Slf4j
public class OAuthAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {


    private final JwtProvider jwtProvider;
    private String Authorization ="Authorization";

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        PrincipalDetails principal = (PrincipalDetails) authentication.getPrincipal();
        String principalLoginId = principal.getLoginId();
        String accessToken = jwtProvider.createAccessToken(principalLoginId);
        response.addHeader(Authorization, accessToken);
        log.info(accessToken);
        getRedirectStrategy().sendRedirect(request, response, "http://localhost:8080/api/user/test");
    }
}
