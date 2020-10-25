package com.github.devsjh.domain.payload.response;

import lombok.Getter;
import lombok.Setter;
import java.util.Date;

/**
 * @memo: 공통 응답 클래스.
 */
@Getter
@Setter
public class CommonResponse {

    private String requestUrl; // 요청 URL.
    private Date requestDate; // 요청 일자 및 시간.

    private Boolean responseStatus; // 응답 성공 여부.
    private String responseCode; // 응답 코드.
    private String responseMessage; // 응답 메시지.

    public CommonResponse() {}
    public CommonResponse(String requestUrl, Boolean responseStatus) {
        this.requestUrl = requestUrl;
        this.requestDate = new Date();
        this.responseStatus = responseStatus;
    }
}