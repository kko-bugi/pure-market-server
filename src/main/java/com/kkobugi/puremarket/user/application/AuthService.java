package com.kkobugi.puremarket.user.application;

import com.kkobugi.puremarket.common.BaseException;
import com.kkobugi.puremarket.common.enums.BaseResponseStatus;
import com.kkobugi.puremarket.user.domain.dto.JwtDto;
import com.kkobugi.puremarket.user.domain.dto.ReissueTokenRequest;
import com.kkobugi.puremarket.user.domain.entity.User;
import io.jsonwebtoken.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.time.Duration;
import java.util.Date;

import static com.kkobugi.puremarket.common.constants.Constant.INACTIVE;
import static com.kkobugi.puremarket.common.constants.Constant.LOGOUT;
import static com.kkobugi.puremarket.common.enums.BaseResponseStatus.*;

/**
 * Token 생성, 분석 및 유효성 검사
 */
@Service
@RequiredArgsConstructor
public class AuthService {
    @Value("${jwt.access-token-validity-in-millis}")
    private int accessTokenExpireTime;

    @Value("${jwt.refresh-token-validity-in-millis}")
    private int refreshTokenExpireTime;
    @Value("${jwt.secret-key}")
    private String secretKey;

    private final RedisTemplate<String, String> redisTemplate;

    // 토큰 발급
    public JwtDto generateToken(User user) {
        String accessToken = generateAccessToken(user);
        String refreshToken = generateRefreshToken(user);
        return new JwtDto(accessToken, refreshToken);
    }

    // accessToken 발급
    public String generateAccessToken(User user) {
        Claims claims = Jwts.claims();
        claims.put("loginId", user.getLoginId());
        claims.put("userIdx", user.getUserIdx());

        // 토큰 생성
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + accessTokenExpireTime))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    // refreshToken 발급
    public String generateRefreshToken(User user) {
        String refreshToken = Jwts.builder()
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + refreshTokenExpireTime))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
        redisTemplate.opsForValue().set("REFRESH_TOKEN:"+user.getLoginId(), refreshToken, Duration.ofMillis(refreshTokenExpireTime));
        return refreshToken;
    }

    // userIdx 추출
    public Long getUserIdxFromToken() throws BaseException {
        String token = getTokenFromRequest();
        if (token == null) throw new BaseException(NULL_ACCESS_TOKEN);
        return getUserIdxFromToken(token);
    }

    // 토큰 추출
    public String getTokenFromRequest() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        else return null;
    }

    public String getLoginIdFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody()
                .get("loginId", String.class);
        //return extractClaims(token, secretKey).get("loginId").toString();
    }

    public Long getUserIdxFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody()
                .get("userIdx", Long.class);
    }

    // 토큰 parsing
    private Claims extractClaims(String token, String secretKey) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
    }

    // 토큰 만료 여부 체크
    public boolean isExpired(String token, String secretKey) {
        Date expiredDate = extractClaims(token, secretKey).getExpiration();
        return expiredDate.before(new Date());
    }

    // 토큰 유효성 체크
    public Boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
            return true;
        } catch (SignatureException ex) {

            System.out.println("Invalid JWT signature");
        } catch (MalformedJwtException ex) {
            System.out.println("Invalid JWT token");
        } catch (ExpiredJwtException ex) {
            System.out.println("Expired JWT token");
        } catch (UnsupportedJwtException ex) {
            System.out.println("Unsupported JWT token");
        } catch (IllegalArgumentException ex) {
            System.out.println("JWT claims string is empty.");
        }
        return false;
    }

    // 로그아웃
    public void logout(User user) throws BaseException {
        // 토큰 유효성 검사
        String accessToken = getTokenFromRequest();
        if (!validateToken(accessToken)) throw new BaseException(BaseResponseStatus.INVALID_ACCESS_TOKEN);

        deleteFromRedis(user);
        registerBlackList(accessToken, LOGOUT);
    }

    // 회원 탈퇴
    public void signout(User user) throws BaseException {
        // 토큰 유효성 검사
        String accessToken = getTokenFromRequest();
        if (!validateToken(accessToken)) throw new BaseException(BaseResponseStatus.INVALID_ACCESS_TOKEN);

        deleteFromRedis(user);
        registerBlackList(accessToken, INACTIVE);
    }

    // redis에 blacklist 등록
    private void registerBlackList(String accessToken, String status) {
        // accessToken expirationTime 동안 staus로 redis에 저장
        Long expirationTime = getExpirationTime(accessToken);
        redisTemplate.opsForValue().set(accessToken, status, Duration.ofMillis(expirationTime));
    }

    // redis에서 refreshToken 삭제
    private void deleteFromRedis(User user) {
        String redisKey = "REFRESH_TOKEN:"+ user.getLoginId();
        if(redisTemplate.opsForValue().get(redisKey) != null) redisTemplate.delete(redisKey);
    }

    // 토큰 유효시간
    public Long getExpirationTime(String token) {
        Date accessTokenExpirationTime = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getExpiration();
        return accessTokenExpirationTime.getTime() - (new Date()).getTime();
    }

    // refreshToken 유효성 체크
    public void validateRefreshToken(ReissueTokenRequest reissueTokenRequest) throws BaseException {
        try {
            String refreshTokenFromRequest = reissueTokenRequest.refreshToken();
            if (refreshTokenFromRequest == null || refreshTokenFromRequest.isEmpty())
                throw new BaseException(INVALID_REFRESH_TOKEN);

            String refreshTokenFromRedis = redisTemplate.opsForValue().get("REFRESH_TOKEN:"+reissueTokenRequest.loginId());
            if (!refreshTokenFromRequest.equals(refreshTokenFromRedis))
                throw new BaseException(INVALID_REFRESH_TOKEN);
        } catch (BaseException e) {
            throw e;
        } catch (Exception e) {
            throw new BaseException(DATABASE_ERROR);
        }
    }
}