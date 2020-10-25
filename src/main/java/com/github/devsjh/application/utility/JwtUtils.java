package com.github.devsjh.application.utility;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.function.Function;

/**
 * @memo: JWT 유틸리티 클래스.
 * https://jwt.io
 */
@Slf4j
@Component
public class JwtUtils {

    @Value("${app.jwt-secret-key}")
    private String JWT_SECRET_KEY;

    // 액세스 토큰 유효 시간(15초).
    // public static final int ACCESS_TOKEN_VALIDATION_SECOND = 15; // 15 sec.
    public static final long ACCESS_TOKEN_VALIDATION_MILLISECOND = 1000L * 15; // 15,000 ms.

    // 리프레시 토큰 유효 시간(60초).
    // public static final int REFRESH_TOKEN_VALIDATION_SECOND = 60; // 300 sec.
    public static final long REFRESH_TOKEN_VALIDATION_MILLISECOND = 1000L * 60; // 60,000 ms.

    // 리프레시 토큰 유효 시간(60분).
    // public static final int REFRESH_TOKEN_VALIDATION_SECOND = 3600; // 3,600 sec.
    // public static final long REFRESH_TOKEN_VALIDATION_MILLISECOND = 1000L * 60 * 60; // 3,600,000 ms.

    // 토큰 이름 상수.
    public static final String ACCESS_TOKEN_NAME = "accessToken";
    public static final String REFRESH_TOKEN_NAME = "refreshToken";

    // 키 암호화.
    private Key getSigningKey(String secretKey) {
        byte[] keyBytes = secretKey.getBytes(StandardCharsets.UTF_8);

        return Keys.hmacShaKeyFor(keyBytes);
    }

    // 토큰을 생성한다.
    public String generateAccessToken(String username) {
        log.info("[INFO] Access token has been created.");
        return generateCommonToken(username, ACCESS_TOKEN_VALIDATION_MILLISECOND);
    }

    // 리프레시 토큰을 생성한다.
    public String generateRefreshToken(String username) {
        log.info("[INFO] Refresh token has been created.");
        return generateCommonToken(username, REFRESH_TOKEN_VALIDATION_MILLISECOND);
    }

    // 토큰 생성 로직.
    public String generateCommonToken(String username, long expireTime) {
        Claims claims = Jwts.claims();
        claims.setSubject(username); // 페이로드에 username 값을 담는다.

        // Map 형식으로도 사용할 수 있다.
        // claims.put("username", username);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expireTime))
                .signWith(getSigningKey(JWT_SECRET_KEY), SignatureAlgorithm.HS256) // 키 값은 256비트 이상이어야 한다. (WeakKeyException)
                .compact();
    }

    // 토큰 검사.
    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = getUsername(token);

        return (username.equals(userDetails.getUsername()) && ! isTokenExpired(token));
    }

    // 토큰에서 계정 ID를 가져온다.
    public String getUsername(String token) {
        return getClaimsFromToken(token, Claims::getSubject);

        // Map 형식으로 사용할 수 있다.
        // return getAllClaimsFromToken(token).get("username", String.class);
    }

    // 토큰 만료 여부를 검사한다.
    private Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);

        // 토큰은 항상 현재 시간보다 이전이어야 한다.
        return expiration.before(new Date());
    }

    // 토큰에서 토큰 만료 시간을 가져온다.
    public Date getExpirationDateFromToken(String token) {
        return getClaimsFromToken(token, Claims::getExpiration);
    }

    // 토큰 만료 시간 또는 계정 ID를 가져온다.
    public <T> T getClaimsFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);

        return claimsResolver.apply(claims);
    }

    // 토큰에서 모든 정보를 가져온다.
    // ExpiredJwtException에서 커스텀 예외로 변경함.
    private Claims getAllClaimsFromToken(String token) throws ExpiredJwtException {
        // 유효한 토큰인지 검사한 후 토큰에 담긴 페이로드 값을 가져온다.
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey(JWT_SECRET_KEY))
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}