package kyonggi.bookslyserver.global.auth.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kyonggi.bookslyserver.domain.user.entity.User;
import kyonggi.bookslyserver.domain.user.repository.UserRepository;
import kyonggi.bookslyserver.global.auth.principal.PrincipalDetails;
import kyonggi.bookslyserver.global.error.ErrorCode;
import kyonggi.bookslyserver.global.error.exception.UnauthorizedException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;

@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;
    private String accessHeader = "Authorization";
    private static final String PREFIX = "Bearer ";


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String accessToken = request.getHeader(accessHeader);

        if (accessToken == null) {
            filterChain.doFilter(request, response);
        }
        else {
            if (!accessToken.startsWith(PREFIX)) {
                throw new UnauthorizedException(ErrorCode.INVALID_ACCESS_TOKEN);}

            accessToken = accessToken.replace(PREFIX, "");

            jwtProvider.validateAccessToken(accessToken);
            String loginId = jwtProvider.extractLoginId(accessToken);

            setAuthentication(loginId);
            filterChain.doFilter(request, response);
        }
    }


    private void setAuthentication(String socialId) {
        User user = userRepository.findByLoginId(socialId)
                .orElseThrow(()-> new UnauthorizedException(ErrorCode.MEMBER_NOT_FOUND));

        // user를 세션에 저장하기 위해 authentication 객체를 생성한다.
        PrincipalDetails principalDetails = new PrincipalDetails(user);
        Authentication authentication = new UsernamePasswordAuthenticationToken(principalDetails, null, principalDetails.getAuthorities());

        // 시큐리티의 세션에 authentication 객체를 저장한다.
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
