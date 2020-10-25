package com.github.devsjh.application.security;

import com.github.devsjh.application.utility.CookieUtils;
import com.github.devsjh.application.utility.JwtUtils;
import com.github.devsjh.application.utility.RedisUtils;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @memo: JWT 인증 필터 클래스.
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final CustomUserDetailsService customUserDetailsService;
    private final JwtUtils jwtUtils;
    private final CookieUtils cookieUtils;
    private final RedisUtils redisUtils;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        final Cookie accessToken = cookieUtils.getCookie(request, JwtUtils.ACCESS_TOKEN_NAME);
        String username = null;
        String accessJwt = null;
        String refreshJwt = null;
        String refreshUsername = null;

        // 액세스 토큰을 검사한다.
        // (쿠키에 액세스 토큰 자체가 없으면 아래 과정이 무시되고 인증을 요청한다. -> 쿠키와 토큰의 유효 시간을 다르게 주는 이유이다.)
        // (쿠키에 액세스 토큰이 있으나 만료되었을 경우 재생성 과정을 수행한다.)
        try {
            if (accessToken != null) {
                accessJwt = accessToken.getValue();
                username = jwtUtils.getUsername(accessJwt);
            }
            if (username != null) {
                UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);

                // 액세스 토큰과 DB의 계정 정보를 사용해 토큰을 검사한다.
                if (jwtUtils.validateToken(accessJwt, userDetails)) {
                    setAuthenticationToken(request, userDetails);
                }
            }
        } catch (ExpiredJwtException e) {
            // log.error("[ERROR] Access token has expired.", e);
            log.error("[ERROR] Access token has expired.");
            // 만료된 액세스 토큰은 재생성 과정이 필요하다. (리프레시 토큰을 사용해 재생성한다.)
            Cookie refreshToken = cookieUtils.getCookie(request, JwtUtils.REFRESH_TOKEN_NAME);

            if (refreshToken != null) {
                refreshJwt = refreshToken.getValue();
            }
        } catch (Exception e) {
            log.error("[ERROR] Could not set access token in security context.");
        }

        // 액세스 토큰을 재생성한다.
        try {
            if (refreshJwt != null) {
                refreshUsername = redisUtils.getData(refreshJwt);

                // 리프레시 토큰과 DB의 계정 정보를 사용해 토큰을 검사한다.
                if (refreshUsername.equals(jwtUtils.getUsername(refreshJwt))) {
                    UserDetails userDetails = customUserDetailsService.loadUserByUsername(refreshUsername);
                    setAuthenticationToken(request, userDetails);

                    String jwt = jwtUtils.generateAccessToken(refreshUsername);
                    Cookie regeneratedAccessToken = cookieUtils.createCookie(JwtUtils.ACCESS_TOKEN_NAME, jwt);

                    response.addCookie(regeneratedAccessToken);
                }
            }
        } catch (ExpiredJwtException e) {
            log.error("[ERROR] Refresh token has expired.");
        } catch (Exception e) {
            log.error("[ERROR] Could not set regenerated access token in security context.");
        }

        // 필터 체인은 리소스 요청 -> 필터 -> ... -> 필터 -> 리소스 반환(응답)의 흐름을 갖는다.
        // 필터 체인의 다음 필터를 호출하도록 한다. (마지막 필터일 경우 필터 실행 후 리소스를 반환한다.)
        filterChain.doFilter(request, response);
    }

    // 토큰 인증 후, 스프링 컨텍스트에 토큰을 세팅한다.
    private void setAuthenticationToken(HttpServletRequest request, UserDetails userDetails) {
        log.info("Username: {}", userDetails.getUsername());
        log.info("Password: {}", userDetails.getPassword());
        log.info("Roles: {}", userDetails.getAuthorities());

        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        authenticationToken.setDetails(
                new WebAuthenticationDetailsSource().buildDetails(request));

        // 인증 토큰 정보 등록.
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
    }
}