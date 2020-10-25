package com.github.devsjh.application.utility;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

/**
 * @memo: 쿠키 유틸리티 클래스.
 */
@Slf4j
@Service
public class CookieUtils {

    // 쿠키 유효 시간(60분; 쿠키 유효 시간을 따로 두는 이유는 필터 클래스에 작성함).
    public static final int COOKIE_VALIDATION_SECOND = 3600;

    // 토큰(쿠키 형식)를 생성한다.
    public Cookie createCookie(String cookieName, String value) {
        Cookie cookie = new Cookie(cookieName, value);
        cookie.setHttpOnly(true);
        cookie.setMaxAge(COOKIE_VALIDATION_SECOND); // maxAge는 초 단위이다.
        cookie.setPath("/");

        log.info("[INFO] Created {} cookie: {}", cookieName, cookie.getValue());
        log.info("[INFO] Cookie max age: {}", cookie.getMaxAge());

        return cookie;
    }

    // 요청에서 쿠키를 꺼낸다.
    public Cookie getCookie(HttpServletRequest request, String cookieName) {
        final Cookie[] cookies = request.getCookies();

        if (cookies == null) return null;
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(cookieName)) {
                return cookie;
            }
        }

        return null;
    }
}