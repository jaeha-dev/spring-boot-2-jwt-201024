package com.github.devsjh.application.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @memo: 빈 유효성 검증 설정 클래스.
 */
@Slf4j
@RequiredArgsConstructor
@Configuration
public class BeanValidatorConfig implements WebMvcConfigurer {

    private final MessageSource messageSource;

    @Bean
    public LocalValidatorFactoryBean getValidator() {
        // 모델 클래스에 설정한 빈 유효성 검증과 메시지 국제화를 혼합하여 사용할 수 있도록 한다.
        LocalValidatorFactoryBean bean = new LocalValidatorFactoryBean();
        bean.setValidationMessageSource(messageSource);

        return bean;
    }
}