package kyonggi.bookslyserver.global.auth.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import kyonggi.bookslyserver.global.error.ErrorCode;
import kyonggi.bookslyserver.global.error.exception.UnauthorizedException;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Getter
@Component
@Slf4j
public class JwtProvider {

    private static final String PREFIX = "Bearer ";
    @Value("${jwt.secret}")
    private String SECRET;
    @Value("${jwt.access-token-expire-time}")
    private long ACCESS_TOKEN_EXPIRE_TIME;

    /**
     * @param loginId 토큰에 담아줄 정보
     * @return 액세스토큰
     */
    public String createAccessToken(String loginId) {
        return PREFIX + JWT.create()
                .withSubject("AccessToken")
                .withExpiresAt(new Date(System.currentTimeMillis() + ACCESS_TOKEN_EXPIRE_TIME))
                .withClaim("loginId", loginId)
                .sign(Algorithm.HMAC512(SECRET));
    }

    public void validateAccessToken(String accessToken) {
        try {
            JWT.require(Algorithm.HMAC512(SECRET)).build().verify(accessToken);
        } catch (JWTVerificationException e) {
            ErrorCode errorCode;
            if (e instanceof TokenExpiredException) {
                errorCode = ErrorCode.EXPIRED_ACCESS_TOKEN;
            } else {
                errorCode = ErrorCode.INVALID_ACCESS_TOKEN_VALUE;
            }
            throw new UnauthorizedException(errorCode);
        }
    }


    public String extractLoginId(String token) {
        return JWT.require(Algorithm.HMAC512(SECRET)).build()
                .verify(token)
                .getClaim("loginId").asString();
    }
}