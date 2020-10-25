package com.github.devsjh.application.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @memo: 응답 인터셉터 클래스.
 */
@Slf4j
@Component
public class ResponseInterceptor implements HandlerInterceptor {

    @Override // 컨트롤러 호출 이전에 동작.
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.info("[INFO] preHandle: {}", request.getRequestURL().toString());
        return true;
    }

    @Override // 컨트롤러 호출 이후에 동작.
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView mv) throws Exception {
        log.info("[INFO] postHandle: {}", request.getRequestURL().toString());
    }

    // @Override // 화면 처리까지 모두 끝난 이후에 동작.
    // public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception e) throws Exception {
    //     log.info("afterCompletion!!");
    // }
}