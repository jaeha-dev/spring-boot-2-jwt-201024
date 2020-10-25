package com.github.devsjh.application.aop.exception;

/**
 * @memo: 로그인 실패 예외 클래스.
 */
public class LoginFailedException extends RuntimeException {

    public LoginFailedException() {
        super();
    }

    public LoginFailedException(String message) {
        super(message);
    }

    public LoginFailedException(String message, Throwable cause) {
        super(message, cause);
    }
}