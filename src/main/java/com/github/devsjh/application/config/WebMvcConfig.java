package com.github.devsjh.application.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @memo: 웹 MVC 설정 클래스.
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    // Preflight 요청 캐싱 시간(30분)
    // (Preflight: 요청을 보내도 되는지 판단하는 것으로 실제 요청 전에 인증 헤더를 전송하여 서버의 허용 여부를 미리 체크하는 테스트 요청이다.)
    private static final long MAX_AGE_SECOND = 1800;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // CORS 활성화 (Global 지정; @CrossOrigin으로 컨트롤러 메소드 레벨에서 지정할 수도 있다.)
        // 클라이언트(www.a.com)가 도메인이 다른 웹 서버(www.b.com; API)에 리소스를 요청하는 것이 교차 출처 요청(Cross Origin Request)이고
        // 웹 서버(API)가 해당 요청을 허용하거나 거절하는 정책이 CORS(Cross Origin Request Share)이다.
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowedMethods("HEAD", "OPTIONS", "GET", "POST", "PUT", "PATCH", "DELETE")
                .maxAge(MAX_AGE_SECOND);
    }
}