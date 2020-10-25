package com.github.devsjh.application.aop.exception;

/**
 * @memo: 로그아웃 실패 예외 클래스.
 */
public class LogoutFailedException extends RuntimeException {

    public LogoutFailedException() {
        super();
    }

    public LogoutFailedException(String message) {
        super(message);
    }

    public LogoutFailedException(String message, Throwable cause) {
        super(message, cause);
    }
}