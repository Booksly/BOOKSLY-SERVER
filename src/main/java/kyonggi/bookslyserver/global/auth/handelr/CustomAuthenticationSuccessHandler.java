package kyonggi.bookslyserver.global.auth.handelr;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kyonggi.bookslyserver.global.auth.principal.shopOwner.OwnerPrincipalDetails;
import kyonggi.bookslyserver.global.common.SuccessCode;
import kyonggi.bookslyserver.global.common.SuccessResponse;
import kyonggi.bookslyserver.global.error.ErrorCode;
import kyonggi.bookslyserver.global.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * 기업 회원의 로그인 요청이 성공했을 경우 작동
 */
@Component
@RequiredArgsConstructor
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private ObjectMapper objectMapper = new ObjectMapper();
    private final JwtUtil jwtUtil;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("utf-8");

        OwnerPrincipalDetails principal = (OwnerPrincipalDetails) authentication.getPrincipal();
        String accessToken = jwtUtil.createAccessToken(principal.getUsername());
        response.addHeader(JwtUtil.Authorization, accessToken);
        response.setStatus(SuccessCode.OK.getHttpStatus().value());
        response.getWriter().write(objectMapper.writeValueAsString(SuccessResponse.of(SuccessCode.OK,"응답 헤더에 담긴 JWT 토큰을 확인해주세요.")));
    }
}
