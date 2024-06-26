package com.example.mdmggreal.global.security;


import com.example.mdmggreal.global.exception.CustomException;
import com.example.mdmggreal.oauth.dto.AuthTokens;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.time.ZonedDateTime;
import java.util.Date;

import static com.example.mdmggreal.global.exception.ErrorCode.*;


@Slf4j
@Component
public class JwtUtil {
    private static Key key = null;
    private final long accessTokenExpTime;
    private final long refreshTokenExpTime;

    public JwtUtil(@Value("${spring.jwt.secret}") String secretKey,
                   @Value("${jwt.access.expiration}") long accessTokenExpTime,
                   @Value("${jwt.refresh.expiration}") long refreshTokenExpTime) {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
        this.accessTokenExpTime = accessTokenExpTime;
        this.refreshTokenExpTime = refreshTokenExpTime;
    }

    public AuthTokens createTokens(CustomUserInfoDto user) {
        String accessToken = createToken(user, accessTokenExpTime);
        String refreshToken = createToken(user, refreshTokenExpTime);
        return AuthTokens.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    public static String createToken(CustomUserInfoDto user, long expireTime) {
        Claims claims = Jwts.claims();
        claims.put("memberId", user.getMemberId());
        ZonedDateTime now = ZonedDateTime.now();
        ZonedDateTime tokenValidTime = now.plusSeconds(expireTime);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(Date.from(now.toInstant()))
                .setExpiration(Date.from(tokenValidTime.toInstant()))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    // 리프레시 토큰으로 새로운 토큰들 재발급
    public AuthTokens refreshTokens(String refreshToken) {
        try {
            validateToken(refreshToken);
            Long memberId = getMemberId(refreshToken);

            // 새로운 액세스 토큰, 리프레시 토큰 생성
            CustomUserInfoDto user = CustomUserInfoDto.of(memberId);
            return createTokens(user);
        } catch (ExpiredJwtException e) {
            throw new CustomException(EXPIRED_JWT_TOKEN);
        }
    }

    public static Long getMemberId(String token) {
        validateToken(token);
        if (token.split(" ").length > 1) {
            token = token.split(" ")[1].trim();
        }
        return parseClaims(token).get("memberId", Long.class);
    }

    public static boolean validateToken(String token) {
        try {
            token = token.split(" ")[1].trim();
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (SignatureException | MalformedJwtException e) {
            log.info("Invalid JWT Token", e);
            throw new CustomException(INVALID_JWT_TOKEN);
        } catch (ExpiredJwtException e) {
            throw new CustomException(EXPIRED_JWT_TOKEN);
        } catch (UnsupportedJwtException e) {
            throw new CustomException(UNSUPPORTED_TOKEN);
        } catch (IllegalArgumentException e) {
            throw new CustomException(JWT_CLAIMS_EMPTY);
        }
    }

    private static Claims parseClaims(String token) {
        try {
            return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
        } catch (ExpiredJwtException e) {
            throw new CustomException(EXPIRED_JWT_TOKEN);
        }
    }
}