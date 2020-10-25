package com.github.devsjh.application.aop.exception;

/**
 * @memo: 잘못된 요청 예외 클래스.
 */
public class BadRequestException extends RuntimeException {

    public BadRequestException() {
        super();
    }

    public BadRequestException(String message) {
        super(message);
    }

    public BadRequestException(String message, Throwable cause) {
        super(message, cause);
    }
}