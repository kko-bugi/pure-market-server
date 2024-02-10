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
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {
    private final UserService userService;
    private final AuthService authService;
    private final RedisTemplate<String, String> redisTemplate;

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("*"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "PATCH", "OPTIONS"));
        //configuration.setAllowCredentials(false);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    protected SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement((sessionManagement) ->
                        sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests((authorizeRequests) -> authorizeRequests
                        .requestMatchers(
                                new AntPathRequestMatcher("/api/v1/users/login"),
                                new AntPathRequestMatcher("/api/v1/users/signup"),
                                new AntPathRequestMatcher("/api/v1/users/nickname"),
                                new AntPathRequestMatcher("/api/v1/users/loginId"),
                                new AntPathRequestMatcher("/api/v1/users/reissue-token"),
                                new AntPathRequestMatcher("/api/v1/produce/**", "GET"),
                                new AntPathRequestMatcher("/api/v1/giveaway/**", "GET"),
                                new AntPathRequestMatcher("/api/v1/recipe/**", "GET"),
                                new AntPathRequestMatcher("/api/v1/home", "GET"),
                                new AntPathRequestMatcher("/api-docs/**"),
                                new AntPathRequestMatcher("/swagger-ui/**"),
                                new AntPathRequestMatcher("/swagger-ui.html"),
                                new AntPathRequestMatcher("/swagger-resources/**")).permitAll()
                        .anyRequest().authenticated())
                .addFilterBefore(new JwtTokenFilter(authService, userService, redisTemplate), UsernamePasswordAuthenticationFilter.class)
                .build();
    }
}
