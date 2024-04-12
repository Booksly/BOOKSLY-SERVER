package kyonggi.bookslyserver.global.config;

import kyonggi.bookslyserver.domain.user.repository.UserRepository;
import kyonggi.bookslyserver.global.auth.handelr.CustomAccessDeniedHandler;
import kyonggi.bookslyserver.global.auth.handelr.OAuthAuthenticationSuccessHandler;
import kyonggi.bookslyserver.global.auth.jwt.ExceptionHandlerFilter;
import kyonggi.bookslyserver.global.auth.handelr.CustomAuthenticationEntryPoint;
import kyonggi.bookslyserver.global.auth.jwt.JwtAuthenticationFilter;
import kyonggi.bookslyserver.global.auth.jwt.JwtProvider;
import kyonggi.bookslyserver.global.auth.principal.PrincipalOAuth2UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.RememberMeConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@RequiredArgsConstructor
@EnableWebSecurity
@Configuration
public class SecurityConfig {
    private final CorsConfig corsConfig;
    private final JwtProvider jwtProvider;
    private final PrincipalOAuth2UserService principalOauth2UserService;
    private final OAuthAuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler;
    private final ExceptionHandlerFilter exceptionHandlerFilter;
    private final UserRepository userRepository;
    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;
    private final CustomAccessDeniedHandler customAccessDeniedHandler;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        /**
         * 시큐리티 설정
         */
        http
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .rememberMe(RememberMeConfigurer::disable)
                .sessionManagement(sessionManagementConfigurer ->
                        sessionManagementConfigurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        /**
         * OAuth 로그인 설정
         */
        http
                .oauth2Login(LoginConfigurer -> LoginConfigurer
                        .userInfoEndpoint(endpointConfig -> endpointConfig.userService(principalOauth2UserService))
                        .successHandler(oAuth2AuthenticationSuccessHandler));

        /**
         * 접근 허용 uri 설정
         */
        http
                .authorizeHttpRequests((authz) -> authz

                        //인증이 필요한 uri - 회원만 접근 가능
                        .requestMatchers("/api/user/need-login","/api/auth/user/**").authenticated()

                        // 그 외 요청, 인증이 필요없음 - 비회원 & 회원 접근 가능
                        .anyRequest().permitAll());

        /**
         * 에러 발생 처리
         */
        http
                .exceptionHandling(exceptionHandlingConfigurer ->
                        exceptionHandlingConfigurer.authenticationEntryPoint(customAuthenticationEntryPoint))
                .exceptionHandling(exceptionHandlingConfigurer->
                        exceptionHandlingConfigurer.accessDeniedHandler(customAccessDeniedHandler));

        /**
         * 필터 추가
         */
        http
                .addFilter(corsConfig.corsFilter())
                .addFilterBefore(new JwtAuthenticationFilter(userRepository,jwtProvider), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(exceptionHandlerFilter, JwtAuthenticationFilter.class);

        return http.build();
    }
}