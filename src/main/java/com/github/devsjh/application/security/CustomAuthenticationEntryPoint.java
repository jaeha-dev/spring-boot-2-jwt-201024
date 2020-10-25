package com.github.devsjh.application.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import javax.annotation.PostConstruct;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @memo: 미인증 접근을 제어하는 엔트리 포인트 클래스.
 */
@Slf4j
@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @PostConstruct
    void init() {
        log.info("[INFO] CustomAuthenticationEntryPoint Component is created.");
    }

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException e) throws IOException, ServletException {
        log.info("[ERROR] Redirect -> /error/entry-point");
        log.info(e.getMessage());

        // 계정 미인증과 관련한 문제가 발생할 경우.
        response.sendRedirect("/error/entry-point");
    }
}