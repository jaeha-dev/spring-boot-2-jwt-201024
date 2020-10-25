package com.github.devsjh.application.aop;

import com.github.devsjh.application.aop.exception.*;
import com.github.devsjh.domain.payload.response.CommonResponse;
import com.github.devsjh.domain.service.CommonResponseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import static com.github.devsjh.application.utility.MessageSourceUtils.getCode;
import static com.github.devsjh.application.utility.MessageSourceUtils.getMessage;

/**
 * @memo: 계정 예외 핸들러 클래스.
 */
@Slf4j
@RequiredArgsConstructor
@Order(Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice(basePackages = "com.github.devsjh.application.web") // 공통 예외처리가 적용될 패키지 지정.
public class UserExceptionHandler {

    private final CommonResponseService commonResponseService;

    // 미인증 사용자 핸들러.
    // @Order(Ordered.HIGHEST_PRECEDENCE)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(AuthenticationRequiredException.class)
    protected CommonResponse handleAuthenticationRequiredException(HttpServletRequest request, HttpServletResponse response, AuthenticationRequiredException e) {
        return commonResponseService.setFailureResponse(
                request.getRequestURL().toString(),
                getCode("user.authentication.required.code"),
                getMessage("user.authentication.required.message")
        );
    }

    // 비인가 사용자 핸들러.
    // @Order(Ordered.HIGHEST_PRECEDENCE)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(AccessDeniedException.class)
    protected CommonResponse handleAccessDeniedException(HttpServletRequest request, HttpServletResponse response, AccessDeniedException e) {
        return commonResponseService.setFailureResponse(
                request.getRequestURL().toString(),
                getCode("user.access.denied.code"),
                getMessage("user.access.denied.message")
        );
    }

    // 만료된 토큰 핸들러.
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(TokenExpiredException.class)
    protected CommonResponse handleTokenExpiredException(HttpServletRequest request, HttpServletResponse response, TokenExpiredException e) {
        return commonResponseService.setFailureResponse(
                request.getRequestURL().toString(),
                getCode("user.token.expired.code"),
                getMessage("user.token.expired.message")
        );
    }

    // 중복된 계정 ID 핸들러.
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(UsernameExistedException.class)
    protected CommonResponse handleUsernameExistedException(HttpServletRequest request, HttpServletResponse response, UsernameExistedException e) {
        return commonResponseService.setFailureResponse(
                request.getRequestURL().toString(),
                getCode("user.username.existed.code"),
                getMessage("user.username.existed.message")
        );
    }

    // 로그인 실패 핸들러.
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(LoginFailedException.class)
    protected CommonResponse handleLoginFailedException(HttpServletRequest request, HttpServletResponse response, LoginFailedException e) {
        return commonResponseService.setFailureResponse(
                request.getRequestURL().toString(),
                getCode("user.login.failed.code"),
                getMessage("user.login.failed.message")
        );
    }

    // 로그아웃 실패 핸들러.
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(LogoutFailedException.class)
    protected CommonResponse handleLogoutFailedException(HttpServletRequest request, HttpServletResponse response, LogoutFailedException e) {
        return commonResponseService.setFailureResponse(
                request.getRequestURL().toString(),
                getCode("user.logout.failed.code"),
                getMessage("user.logout.failed.message")
        );
    }
}