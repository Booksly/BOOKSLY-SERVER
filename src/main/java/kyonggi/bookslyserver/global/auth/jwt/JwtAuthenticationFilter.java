package kyonggi.bookslyserver.global.auth.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kyonggi.bookslyserver.domain.user.constant.Role;
import kyonggi.bookslyserver.domain.user.entity.ShopOwner;
import kyonggi.bookslyserver.domain.user.entity.User;
import kyonggi.bookslyserver.domain.user.service.ShopOwnerService;
import kyonggi.bookslyserver.domain.user.service.UserQueryService;
import kyonggi.bookslyserver.global.auth.principal.shopOwner.OwnerPrincipalDetails;
import kyonggi.bookslyserver.global.auth.principal.user.OAuthPrincipalDetails;
import kyonggi.bookslyserver.global.error.ErrorCode;
import kyonggi.bookslyserver.global.error.exception.UnauthorizedException;
import kyonggi.bookslyserver.global.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;

import static kyonggi.bookslyserver.global.util.JwtUtil.Authorization;
import static kyonggi.bookslyserver.global.util.JwtUtil.PREFIX;


@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserQueryService userQueryService;
    private final ShopOwnerService shopOwnerService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String accessToken = request.getHeader(Authorization);

        if (accessToken == null) {
            filterChain.doFilter(request, response);
        }
        else {
            if (!accessToken.startsWith(PREFIX)) {
                throw new UnauthorizedException(ErrorCode.INVALID_ACCESS_TOKEN);}

            accessToken = accessToken.replace(PREFIX, "");

            jwtUtil.validateAccessToken(accessToken);
            String loginId = jwtUtil.extractLoginId(accessToken);

            setAuthentication(loginId);
            filterChain.doFilter(request, response);
        }
    }


    private void setAuthentication(String loginId) {
        Authentication authentication;

        User user = userQueryService.findUserByLoginId(loginId);

        if (user.getRole() == Role.ROLE_ADMIN) {
            ShopOwner shopOwner = shopOwnerService.findShopOwnerByUserId(user.getId());
            shopOwner.addUser(user);
            OwnerPrincipalDetails ownerPrincipalDetails = new OwnerPrincipalDetails(shopOwner);
            authentication = new UsernamePasswordAuthenticationToken(ownerPrincipalDetails, null, ownerPrincipalDetails.getAuthorities());
        } else {
            OAuthPrincipalDetails oAuthPrincipalDetails = new OAuthPrincipalDetails(user);
            authentication = new UsernamePasswordAuthenticationToken(oAuthPrincipalDetails, null, oAuthPrincipalDetails.getAuthorities());
        }

        // 시큐리티의 세션에 authentication 객체를 저장
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
