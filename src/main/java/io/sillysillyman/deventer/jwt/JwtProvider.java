package io.sillysillyman.deventer.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import io.sillysillyman.deventer.enums.UserRole;
import io.sillysillyman.deventer.exception.InvalidTokenException;
import io.sillysillyman.deventer.exception.TokenExpiredException;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import java.security.Key;
import java.util.Base64;
import java.util.Date;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
@Slf4j(topic = "JwtProvider")
public class JwtProvider {

    public static final String ACCESS_HEADER = "Authorization";
    public static final String REFRESH_HEADER = "X-Refresh-Token";
    public static final String AUTHORIZATION_KEY = "auth";
    public static final String BEARER_PREFIX = "Bearer ";
    private static final long ACCESS_TOKEN_EXPIRATION_TIME = 30 * 60 * 1000L; // 30분
    private static final long REFRESH_TOKEN_EXPIRATION_TIME = 7 * 24 * 60 * 60 * 1000L; // 7일
    private static final SignatureAlgorithm SIGNATURE_ALGORITHM = SignatureAlgorithm.HS256;

    @Value("${jwt.secret.key}")
    private String secretKey;

    private Key key;

    @PostConstruct
    public void init() {
        byte[] bytes = Base64.getDecoder().decode(secretKey);
        key = Keys.hmacShaKeyFor(bytes);
    }

    // 토큰 생성
    public String createAccessToken(String username, UserRole role) {
        return createToken(username, role, ACCESS_TOKEN_EXPIRATION_TIME);
    }

    // 리프레시 토큰 생성
    public String createRefreshToken(String username) {
        return createToken(username, null, REFRESH_TOKEN_EXPIRATION_TIME);
    }

    // header 에서 JWT 가져오기
    public String getJwtFromHeader(HttpServletRequest request, String headerName) {
        String token = request.getHeader(headerName);
        if (StringUtils.hasText(token) && token.startsWith(BEARER_PREFIX)) {
            return token.substring(7);
        }
        return null;
    }

    // 토큰 검증
    public void validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
        } catch (SecurityException | MalformedJwtException e) {
            log.error("Invalid JWT signature, 유효하지 않는 JWT 서명 입니다.");
            throw new InvalidTokenException("유효하지않은 토큰입니다.");
        } catch (ExpiredJwtException e) {
            log.error("Expired JWT token, 만료된 JWT token 입니다.");
            throw new TokenExpiredException("만료된 토큰입니다.");
        } catch (UnsupportedJwtException e) {
            log.error("Unsupported JWT token, 지원되지 않는 JWT 토큰 입니다.");
            throw new InvalidTokenException("유효하지않은 토큰입니다.");
        } catch (IllegalArgumentException e) {
            log.error("JWT claims is empty, 잘못된 JWT 토큰 입니다.");
            throw new InvalidTokenException("유효하지않은 토큰입니다.");
        }
    }

    // 토큰에서 사용자 정보 가져오기
    public String getUsername(String token) {
        Claims claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token)
            .getBody();
        return claims.getSubject();
    }

    private String createToken(String username, UserRole role, long expirationTime) {
        Date now = new Date();

        Claims claims = Jwts.claims().setSubject(username).setIssuedAt(now);
        if (role != null) {
            claims.put(AUTHORIZATION_KEY, role);
        }

        return BEARER_PREFIX + Jwts.builder()
            .setClaims(claims)
            .setExpiration(new Date(now.getTime() + expirationTime))
            .signWith(key, SIGNATURE_ALGORITHM)
            .compact();
    }
}