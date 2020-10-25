package com.github.devsjh.domain.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;
import java.time.Instant;

/**
 * @memo: 모든 엔터티에 적용되는 컬럼을 추출한 클래스.
 * (생성/갱신 일자는 모든 테이블에서 필요한 컬럼이다.)
 */
@Getter
@Setter
@MappedSuperclass // 하위 엔터티에 상속함을 알림.
@EntityListeners(AuditingEntityListener.class) // Audit 기능 포함.
public abstract class DateAudit implements Serializable {

    @CreatedDate // 엔터티가 생성되어 저장될 때 시간.
    @Column(nullable = false, updatable = false)
    private Instant createdAt; // 생성 일자.

    @LastModifiedDate // 엔터티를 변경하여 갱신될 때 시간.
    @Column(nullable = false)
    private Instant updatedAt; // 갱신 일자.

    // createdBy, updatedBy.
}