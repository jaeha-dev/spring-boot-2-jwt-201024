package com.github.devsjh;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * @memo: 애플리케이션 실행을 위한 부트스트랩 클래스.
 */
@Slf4j
@SpringBootApplication
@EntityScan(basePackageClasses = {
        DevsjhApplication.class,
        Jsr310JpaConverters.class
})
public class DevsjhApplication {

    @PostConstruct // 의존성 주입이 완료된 후 작업을 수행함.
    void init() {
        final Date currentTime = new Date();
        final SimpleDateFormat sdf = new SimpleDateFormat("EEE, d MMM yyyy hh:mm:ss z"); // Sat, 17 Oct 2020 11:23:44 GMT;
        sdf.setTimeZone(TimeZone.getTimeZone("GMT"));

        log.info("GMT time: " + sdf.format(currentTime));
        // TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
    }

    public static void main(String[] args) {
        SpringApplication.run(DevsjhApplication.class, args);
    }

    @PreDestroy // 스프링 컨테이너에서 빈을 제거하기 전 작업을 수행함.
    void destroy() {
        // ...
    }
}