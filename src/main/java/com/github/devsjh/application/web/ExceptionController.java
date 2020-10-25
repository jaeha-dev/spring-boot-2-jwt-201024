package com.github.devsjh.application.web;

import com.github.devsjh.application.aop.exception.AuthenticationRequiredException;
import com.github.devsjh.domain.payload.response.CommonResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @memo: 예외 컨트롤러 클래스.
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/error")
public class ExceptionController implements ErrorController {

    @GetMapping("/entry-point")
    public CommonResponse entryPointException() {
        log.error("[ERROR] Login is required.");
        throw new AuthenticationRequiredException();
    }

    @GetMapping("/access-denied")
    public CommonResponse accessDeniedException() {
        log.error("[ERROR] You don't have access to that resource.");
        throw new AccessDeniedException("");
    }

    @Override
    public String getErrorPath() {
        return "/error";
    }
}