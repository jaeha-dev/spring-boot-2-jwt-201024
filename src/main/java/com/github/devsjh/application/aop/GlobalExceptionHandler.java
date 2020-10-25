package com.github.devsjh.application.aop;

import com.github.devsjh.application.aop.exception.BadRequestException;
import com.github.devsjh.application.aop.exception.ResourceNotFoundException;
import com.github.devsjh.domain.payload.response.CommonResponse;
import com.github.devsjh.domain.service.CommonResponseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import static com.github.devsjh.application.utility.MessageSourceUtils.getCode;
import static com.github.devsjh.application.utility.MessageSourceUtils.getMessage;

/**
 * @memo: 전역 예외 핸들러 클래스.
 */
@Slf4j
@RequiredArgsConstructor
@Order(Ordered.LOWEST_PRECEDENCE)
@RestControllerAdvice(basePackages = "com.github.devsjh.application.web") // 공통 예외처리가 적용될 패키지 지정.
public class GlobalExceptionHandler {

    private final CommonResponseService commonResponseService;

    // 최상위 핸들러. (다른 핸들러에서 잡지 못한 예외를 처리한다.)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler({Exception.class, RuntimeException.class}) // 두 개 이상의 예외를 받을 수 있음을 표현하기 위해 씀.
    protected CommonResponse handleException(HttpServletRequest request, HttpServletResponse response, Exception e) {
        return commonResponseService.setFailureResponse(
                request.getRequestURL().toString(),
                getCode("server.error.occurred.code"),
                getMessage("server.error.occurred.message")
        );
    }

    // 잘못된 요청 핸들러.
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BadRequestException.class)
    protected CommonResponse handleBadRequestException(HttpServletRequest request, HttpServletResponse response, BadRequestException e) {
        return commonResponseService.setFailureResponse(
                request.getRequestURL().toString(),
                getCode("server.bad.request.code"),
                getMessage("server.bad.request.message")
        );
    }

    // 리소스 조회 실패 핸들러.
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(ResourceNotFoundException.class)
    protected CommonResponse handleResourceNotFoundException(HttpServletRequest request, HttpServletResponse response, ResourceNotFoundException e) {
        return commonResponseService.setFailureResponse(
                request.getRequestURL().toString(),
                getCode("server.resource.not.found.code"),
                getMessage("server.resource.not.found.message")
        );
    }
}