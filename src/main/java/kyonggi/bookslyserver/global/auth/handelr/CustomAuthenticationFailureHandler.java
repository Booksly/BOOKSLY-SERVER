package kyonggi.bookslyserver.global.auth.handelr;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kyonggi.bookslyserver.global.error.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * 기업회원의 로그인 요청이 실패 시 작동
 */
@Component
@Slf4j
public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("utf-8");
        response.setStatus(ErrorCode.LOGIN_FAILURE .getHttpStatus().value());
        log.info(exception.getMessage());
        response.getWriter().write(objectMapper.writeValueAsString(ErrorCode.LOGIN_FAILURE.getErrorReason()));
    }
}
