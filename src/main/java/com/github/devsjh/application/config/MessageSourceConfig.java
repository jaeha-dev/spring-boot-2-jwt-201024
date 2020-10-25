package com.github.devsjh.application.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import java.util.*;

/**
 * @memo: 메시지 국제화 설정 클래스.
 */
@Slf4j
@Configuration
public class MessageSourceConfig implements WebMvcConfigurer {

    @Bean
    public LocaleResolver localeResolver() {
        // 지역 설정.
        // SessionLocaleResolver resolver = new SessionLocaleResolver();
        CookieLocaleResolver resolver = new CookieLocaleResolver();
        resolver.setDefaultLocale(Locale.KOREAN);
        // resolver.setDefaultLocale(Locale.ENGLISH);

        return resolver;
    }

    @Bean
    public LocaleChangeInterceptor localeChangeInterceptor() {
        // 지역 설정을 변경하는 인터셉터. (요청 시, lang 정보를 지정하여 전달하면 지역 언어가 변경된다.)
        LocaleChangeInterceptor interceptor = new LocaleChangeInterceptor();
        // interceptor.setParamName("language");
        interceptor.setParamName("lang");

        return interceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(localeChangeInterceptor());
    }

    @Bean
    public MessageSource messageSource(@Value("${spring.messages.basename}") String[] baseNames,
                                       @Value("${spring.messages.encoding}") String encoding) {

        // 메시지 프로퍼티 파일을 참조하는 MessageSource 선언.
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasenames(baseNames);
        messageSource.setDefaultEncoding(encoding);
        messageSource.setCacheSeconds(30); // 메시지 리소스를 캐싱하는 최대 시간 지정.

        return messageSource;
    }
}