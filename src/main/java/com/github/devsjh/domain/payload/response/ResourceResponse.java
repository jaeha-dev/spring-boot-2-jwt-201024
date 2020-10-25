package com.github.devsjh.domain.payload.response;

import lombok.Getter;
import lombok.Setter;

/**
 * @memo: 리소스 응답 클래스.
 */
@Getter
@Setter
public class ResourceResponse<T> extends CommonResponse {

    // Boolean status, String code, String message, Date date, String referredUrl.
    private T responseResource; // 응답 리소스.

    public ResourceResponse(String requestUrl, Boolean responseStatus) {
        super(requestUrl, responseStatus);
    }
}