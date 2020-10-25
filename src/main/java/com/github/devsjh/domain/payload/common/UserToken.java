package com.github.devsjh.domain.payload.common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import javax.servlet.http.Cookie;

/**
 * @memo: 액세스, 리프레시 토큰 페이로드 클래스.
 */
@Getter
@Setter
@AllArgsConstructor
public class UserToken {

    private Cookie accessToken;
    private Cookie refreshToken;
}