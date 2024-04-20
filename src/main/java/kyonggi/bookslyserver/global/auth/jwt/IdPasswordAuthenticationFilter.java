package kyonggi.bookslyserver.global.auth.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kyonggi.bookslyserver.domain.user.dto.request.ShopOwnerLoginRequestDto;
import kyonggi.bookslyserver.global.auth.principal.shopOwner.OwnerPrincipalDetails;
import kyonggi.bookslyserver.global.common.SuccessCode;
import kyonggi.bookslyserver.global.error.exception.EntityNotFoundException;
import kyonggi.bookslyserver.global.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;

@RequiredArgsConstructor
@Slf4j
public class IdPasswordAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager manager;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        ObjectMapper mapper = new ObjectMapper();
        ShopOwnerLoginRequestDto shopOwner = null;
        try {
            shopOwner = mapper.readValue(request.getInputStream(), ShopOwnerLoginRequestDto.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(shopOwner.loginId(), shopOwner.password());
        Authentication authenticated = manager.authenticate(token);

        return authenticated;

    }
}
