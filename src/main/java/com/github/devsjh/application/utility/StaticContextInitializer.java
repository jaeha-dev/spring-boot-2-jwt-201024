package com.github.devsjh.application.utility;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import javax.annotation.PostConstruct;

/**
 * @memo: 메시지 관련 유틸리티 클래스를 위한 초기화 클래스.
 * @memo: https://better-coding.com/spring-how-to-autowire-bean-in-a-static-class
 */
@RequiredArgsConstructor
@Component
public class StaticContextInitializer {

    private final ApplicationContext applicationContext;
    private final MessageSource messageSource;

    @PostConstruct
    public void init() {
        MessageSourceUtils.setMessageSource(messageSource);
    }
}