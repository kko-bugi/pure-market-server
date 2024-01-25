package com.kkobugi.puremarket.common.configuration;

import com.kkobugi.puremarket.user.application.AuthService;
import com.kkobugi.puremarket.user.application.UserService;
import com.kkobugi.puremarket.user.utils.JwtTokenFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {
    private final UserService userService;
    private final AuthService authService;
    private final RedisTemplate<String, String> redisTemplate;

    @Bean
    protected SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement((sessionManagement) ->
                        sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests((authorizeRequests) -> authorizeRequests
                        .requestMatchers(
                                new AntPathRequestMatcher("/api/v1/users/login"),
                                new AntPathRequestMatcher("/api/v1/users/signup"),
                                new AntPathRequestMatcher("/api/v1/users/nickname"),
                                new AntPathRequestMatcher("/api/v1/users/loginId")).permitAll()
                        .anyRequest().authenticated()) //TODO: 마이페이지 접근 권한 설정 추가
                .addFilterBefore(new JwtTokenFilter(authService, userService, redisTemplate), UsernamePasswordAuthenticationFilter.class) // 필터 통과
                .build();
    }
}
