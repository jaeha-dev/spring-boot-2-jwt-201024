package com.github.devsjh.application.config;

import com.github.devsjh.application.interceptor.ResponseInterceptor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @memo: 인터셉터 설정 클래스.
 */
@Slf4j
@RequiredArgsConstructor
@Configuration
public class InterceptorConfig implements WebMvcConfigurer {

    private final ResponseInterceptor responseInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(responseInterceptor)
                .addPathPatterns("/api/user/**"); // 인터셉터가 적용될 URL 범위 지정.
    }
}