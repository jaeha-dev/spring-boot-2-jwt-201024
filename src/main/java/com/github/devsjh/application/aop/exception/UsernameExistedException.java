package com.github.devsjh.application.aop.exception;

/**
 * @memo: 계정 ID 중복 예외 클래스.
 */
public class UsernameExistedException extends RuntimeException {

    public UsernameExistedException() {
        super();
    }

    public UsernameExistedException(String message) {
        super(message);
    }

    public UsernameExistedException(String message, Throwable cause) {
        super(message, cause);
    }
}