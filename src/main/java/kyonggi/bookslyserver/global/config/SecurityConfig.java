package kyonggi.bookslyserver.global.config;

import kyonggi.bookslyserver.domain.user.service.ShopOwnerService;
import kyonggi.bookslyserver.domain.user.service.UserService;
import kyonggi.bookslyserver.global.auth.handelr.CustomAccessDeniedHandler;
import kyonggi.bookslyserver.global.auth.handelr.CustomAuthenticationSuccessHandler;
import kyonggi.bookslyserver.global.auth.handelr.OAuthAuthenticationSuccessHandler;
import kyonggi.bookslyserver.global.auth.jwt.ExceptionHandlerFilter;
import kyonggi.bookslyserver.global.auth.handelr.CustomAuthenticationEntryPoint;
import kyonggi.bookslyserver.global.auth.jwt.IdPasswordAuthenticationFilter;
import kyonggi.bookslyserver.global.auth.jwt.JwtAuthenticationFilter;
import kyonggi.bookslyserver.global.auth.principal.user.OAuthPrincipalService;
import kyonggi.bookslyserver.global.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.RememberMeConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@RequiredArgsConstructor
@EnableWebSecurity
@Configuration
public class SecurityConfig {
    private final CorsConfig corsConfig;
    private final JwtUtil jwtUtil;
    private final OAuthPrincipalService oauthPrincipalService;
    private final OAuthAuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler;
    private final ExceptionHandlerFilter exceptionHandlerFilter;
    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;
    private final CustomAccessDeniedHandler customAccessDeniedHandler;
    private final AuthenticationFailureHandler customAuthenticationFailureHandler;
    private final CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler;
    private final UserDetailsService userDetailsService;
    private final ShopOwnerService shopOwnerService;
    private final UserService userService;


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        AuthenticationManagerBuilder sharedObject = http.getSharedObject(AuthenticationManagerBuilder.class);
        sharedObject.userDetailsService(this.userDetailsService);
        AuthenticationManager authenticationManager = sharedObject.build();

        http
                .authenticationManager(authenticationManager);

        /**
         * 시큐리티 설정
         */
        http
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .rememberMe(RememberMeConfigurer::disable)
                .sessionManagement(sessionManagementConfigurer ->
                        sessionManagementConfigurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                ;

        /**
         * OAuth 로그인 설정
         */
        http
                .oauth2Login(LoginConfigurer -> LoginConfigurer
                        .userInfoEndpoint(endpointConfig -> endpointConfig.userService(oauthPrincipalService))
                        .successHandler(oAuth2AuthenticationSuccessHandler));

        /**
         * 접근 허용 uri 설정
         */
        http
                .authorizeHttpRequests((authz) -> authz

                        //인증이 필요한 uri - 일반 회원만 접근 가능
                        .requestMatchers("/api/user/details/**","/api/auth/verify/user/**","api/reservations").hasRole("USER")
                        //인증이 필요한 uri - 기업 회원만 접근 가능
                        .requestMatchers("/api/owner/**","/api/events/**","/api/shops/**","/api/employees/{employeeId}/reservations}","api/reservations/owner/**").hasRole("ADMIN")
                        // 그 외 요청, 인증이 필요없음 - 비회원도 접근 가능
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

        IdPasswordAuthenticationFilter idPasswordAuthenticationFilter = new IdPasswordAuthenticationFilter(authenticationManager);
        idPasswordAuthenticationFilter.setAuthenticationFailureHandler(customAuthenticationFailureHandler);
        idPasswordAuthenticationFilter.setAuthenticationSuccessHandler(customAuthenticationSuccessHandler);

        http
                .addFilter(corsConfig.corsFilter())
                .addFilter(idPasswordAuthenticationFilter)
                .addFilterBefore(new JwtAuthenticationFilter(jwtUtil,userService,shopOwnerService), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(exceptionHandlerFilter, JwtAuthenticationFilter.class);

        return http.build();
    }

}