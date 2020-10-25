package com.github.devsjh.application.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * @memo: JPA Audit 활성화 설정 클래스.
 */
@Configuration
@EnableJpaAuditing
public class JpaAuditConfig {
}