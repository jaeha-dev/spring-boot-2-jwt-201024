package com.github.devsjh.application.aop.exception;

/**
 * @memo: 토큰 만료 예외 클래스.
 */
public class TokenExpiredException extends RuntimeException {

    public TokenExpiredException() {
        super();
    }

    public TokenExpiredException(String message) {
        super(message);
    }

    public TokenExpiredException(String message, Throwable cause) {
        super(message, cause);
    }
}