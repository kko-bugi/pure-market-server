package com.kkobugi.puremarket.user.application;

import com.kkobugi.puremarket.user.domain.entity.User;
import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * Token 생성, 분석 및 유효성 검사
 */
@Service
@RequiredArgsConstructor
public class AuthService {
    @Value("${jwt.token-validity-in-seconds}")
    private int expireTime;
    @Value("${jwt.secret-key}")
    private String secretKey;

    // 토큰 발급
    public String generateAccessToken(User user) {
        Claims claims = Jwts.claims();
        claims.put("loginId", user.getLoginId());
        claims.put("userIdx", user.getUserIdx());

        // 토큰 생성
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expireTime))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
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

    // 토큰 만료 시간 체크
    public boolean isExpired(String token, String secretKey) {
        Date expiredDate = extractClaims(token, secretKey).getExpiration();
        return expiredDate.before(new Date());
    }

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
}