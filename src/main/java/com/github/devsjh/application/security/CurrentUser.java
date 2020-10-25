package com.github.devsjh.application.security;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import java.lang.annotation.*;

/**
 * @memo: 커스텀 어노테이션 클래스
 */
@Target({ElementType.PARAMETER, ElementType.TYPE}) // 사용할 수 있는 위치(메소드의 인자, 클래스/인터페이스/열거형)를 지정한다.
@Retention(RetentionPolicy.RUNTIME) // 컴파일 이후에도 JVM에 의해서 참조 가능하도록 설정한다.
@Documented // Java Doc으로 API 문서를 만들 때 어노테이션에 대한 설명을 포함하도록 지정한다.
@AuthenticationPrincipal
public @interface CurrentUser {

    // 참고로 인증되지 않은 사용자는 Authentication 객체가 가진 Principal의 문자열이 anonymousUser이다.
    // 따라서 인증되지 않은 사용자인 경우 null을, 아니면 user 객체를 반환한다.
}