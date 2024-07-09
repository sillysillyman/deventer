package io.sillysillyman.deventer.config;

import io.sillysillyman.deventer.enums.UserRole;
import io.sillysillyman.deventer.security.JwtAuthenticationFilter;
import io.sillysillyman.deventer.security.JwtExceptionFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final JwtExceptionFilter jwtExceptionFilter;
    private final AuthenticationProvider authenticationProvider;

    /**
     * SecurityFilterChain을 구성하는 메서드입니다.
     *
     * @param http HttpSecurity 객체
     * @return SecurityFilterChain 객체
     * @throws Exception 예외가 발생할 수 있음
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // CORS와 CSRF 설정 비활성화
        http.cors(AbstractHttpConfigurer::disable)
            .csrf(AbstractHttpConfigurer::disable);

        // 기본 설정인 세션 방식은 사용하지 않고 JWT 방식을 사용하기 위한 설정
        http.sessionManagement((sessionManagement) ->
            sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        // 요청에 대한 권한 설정
        http.authorizeHttpRequests((authorizeHttpRequests) ->
            authorizeHttpRequests.requestMatchers(
                    PathRequest.toStaticResources().atCommonLocations()).permitAll()
                .requestMatchers("/auth/refresh", "/auth/login", "/auth/signup").permitAll()
                .requestMatchers(HttpMethod.GET).permitAll() // Post, Comment 조회기능만 통과하도록 추후 변경
                .requestMatchers("/admin/**").hasAuthority(UserRole.ADMIN.getAuthority())
                .anyRequest().authenticated()
        ).authenticationProvider(authenticationProvider);

        // 필터 관리
        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        http.addFilterBefore(jwtExceptionFilter, JwtAuthenticationFilter.class);

        return http.build();
    }
}