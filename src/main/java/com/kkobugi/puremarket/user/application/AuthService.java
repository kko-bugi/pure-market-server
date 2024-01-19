package com.kkobugi.puremarket.user.application;

import com.kkobugi.puremarket.user.domain.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class AuthService {
    private static final int expireTime = 864000;
    @Value("${jwt.secret-key}")
    private static String key;

    // 토큰 발급
    public static String createToken(User user) {
        Claims claims = Jwts.claims();
        claims.put("loginId", user.getLoginId()); // claims에 loginId 저장

        // 토큰 생성
        String accessToken =  Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expireTime))
                .signWith(SignatureAlgorithm.HS256, key)
                .compact();
        return accessToken;
    }

    public static String getLoginId(String token, String secretKey) {
        return extractClaims(token, secretKey).get("loginId").toString();
    }

    // 토큰 parsing
    private static Claims extractClaims(String token, String secretKey) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
    }

    // 토큰 만료 시간 체크
    public static boolean isExpired(String token, String secretKey) {
        Date expiredDate = extractClaims(token, secretKey).getExpiration();
        return expiredDate.before(new Date());
    }
}