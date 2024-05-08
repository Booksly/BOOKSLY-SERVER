package kyonggi.bookslyserver.global.auth.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kyonggi.bookslyserver.global.error.ErrorCode;
import kyonggi.bookslyserver.global.error.dto.ErrorResponseDto;
import kyonggi.bookslyserver.global.error.exception.InvalidValueException;
import kyonggi.bookslyserver.global.error.exception.UnauthorizedException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.http.MediaType;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.nio.file.AccessDeniedException;


@Slf4j
@Component
public class ExceptionHandlerFilter extends OncePerRequestFilter {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            //JwtAuthenticationFilter 실행
            filterChain.doFilter(request, response);
        } catch (UnauthorizedException e) {
            handleUnauthorizedException(response, e);
        } catch (AccessDeniedException e) {
            log.info(e.getMessage());
        } catch (Exception e) {
            handleException(e, response);
        }
    }

    private void handleUnauthorizedException(HttpServletResponse response, Exception e) throws IOException {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("utf-8");
        if (e instanceof UnauthorizedException ue) {
            response.setStatus(ue.getErrorCode().getHttpStatus().value());
            response.getWriter().write(objectMapper.writeValueAsString(ue.getErrorCode().getErrorReason()));
        } else if (e instanceof InvalidValueException ie) {
            response.setStatus(ie.getErrorCode().getHttpStatus().value());
            response.getWriter().write(objectMapper.writeValueAsString(ie.getErrorCode().getErrorReason()));
        }
    }

    private void handleException(Exception e,HttpServletResponse response) throws IOException {
        log.info(e.getMessage());
//        log.info(e.getCause().toString());
//        log.info(e.getStackTrace().toString());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("utf-8");
        response.setStatus(ErrorCode.INTERNAL_SERVER_ERROR.getHttpStatus().value());
        response.getWriter().write(objectMapper.writeValueAsString(ErrorCode.INTERNAL_SERVER_ERROR.getErrorReason()));
    }
}