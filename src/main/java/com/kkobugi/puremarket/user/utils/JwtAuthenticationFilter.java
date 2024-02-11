package com.kkobugi.puremarket.user.utils;

import com.kkobugi.puremarket.user.application.AuthService;
import com.kkobugi.puremarket.user.application.UserService;
import com.kkobugi.puremarket.user.domain.entity.User;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final AuthService authService;
    private final UserService userService;
    private final RedisTemplate<String, String> redisTemplate;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {
        String accessToken = getTokenFromRequest(request);
        if (accessToken != null && authService.validateToken(accessToken)) { // validation
            String isLogout = redisTemplate.opsForValue().get(accessToken);

            // logout 상태가 아닌 경우
            if (ObjectUtils.isEmpty(isLogout)) {
                UsernamePasswordAuthenticationToken authentication = getAuthenticationFromToken(accessToken);
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication); // 권한 부여
            }
        }
        filterChain.doFilter(request, response);
    }

    private String getTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        else return null;
    }

    private UsernamePasswordAuthenticationToken getAuthenticationFromToken(String token) {
        Long userIdx = authService.getUserIdxFromToken(token);
        User user = userService.getUserByUserIdx(userIdx);
        return new UsernamePasswordAuthenticationToken(user, null, null);
    }

//    // 추후 인가 필요 시 사용
//    private UsernamePasswordAuthenticationToken getAuthenticationFromToken(String token) {
//        Long userIdx = authService.getUserIdxFromToken(token);
//        UserDetails userDetails = customUserDetailsService.loadUserById(userIdx);
//        return new UsernamePasswordAuthenticationToken(userDetails, null, null);
//    }
}
