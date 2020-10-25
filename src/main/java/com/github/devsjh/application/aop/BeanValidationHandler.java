package com.github.devsjh.application.aop;

import com.github.devsjh.domain.payload.response.CommonResponse;
import com.github.devsjh.domain.service.CommonResponseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * @memo: 입력값 유효성 예외 핸들러 클래스.
 * https://www.baeldung.com/spring-boot-bean-validation
 * https://www.baeldung.com/spring-custom-validation-message-source
 */
@Slf4j
@RequiredArgsConstructor
@Order(Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice(basePackages = "com.github.devsjh.application.web") // 공통 예외처리가 적용될 패키지 지정.
public class BeanValidationHandler {

    private final CommonResponseService commonResponseService;

    // 입력값 유효성 실패 핸들러.
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected CommonResponse handleValidationException(HttpServletRequest request, HttpServletResponse response, MethodArgumentNotValidException e) {
        Map<String, String> errors = new HashMap<>();

        e.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        return commonResponseService.setFailureResponse(
                request.getRequestURL().toString(),
                errors
        );
    }
}