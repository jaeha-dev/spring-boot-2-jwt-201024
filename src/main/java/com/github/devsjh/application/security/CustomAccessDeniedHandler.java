package com.github.devsjh.application.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;
import javax.annotation.PostConstruct;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @memo: 비인가 접근을 제어하는 핸들러 클래스.
 */
@Slf4j
@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    @PostConstruct
    void init() {
        log.info("[INFO] CustomAccessDeniedHandler Component is created.");
    }

    @Override
    public void handle(HttpServletRequest request,
                       HttpServletResponse response,
                       AccessDeniedException e) throws IOException, ServletException {
        log.info("[ERROR] Redirect -> /error/access-denied");
        log.info(e.getMessage());

        // 비인가 접근과 관련한 문제가 발생할 경우.
        response.sendRedirect("/error/access-denied");
    }
}