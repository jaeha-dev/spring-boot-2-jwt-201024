package com.github.devsjh.domain.service;

import com.github.devsjh.domain.payload.response.*;
import org.springframework.stereotype.Service;

/**
 * @memo: 응답 서비스 클래스.
 */
@Service
public class CommonResponseService {

    public enum ResponseType {
        // 기본 코드, 메시지 설정.
        SUCCESS("0000", "Success"),
        FAILURE("-0000", "Failure");

        private final String code;
        private final String message;

        ResponseType(String code, String message) {
            this.code = code;
            this.message = message;
        }

        public String getCode() {
            return code;
        }

        public String getMessage() {
            return message;
        }
    }

    // 성공 응답. (기본 코드/메시지 사용.)
    public CommonResponse setSuccessResponse(String url) {
        CommonResponse response = new CommonResponse(url, true);
        response.setResponseCode(ResponseType.SUCCESS.getCode());
        response.setResponseMessage(ResponseType.SUCCESS.getMessage());

        return response;
    }

    // 성공 응답. (커스텀 코드/메시지 사용.)
    public CommonResponse setSuccessResponse(String url, String code, String message) {
        CommonResponse response = new CommonResponse(url, true);
        response.setResponseCode(code);
        response.setResponseMessage(message);

        return response;
    }

    // 성공 응답. (기본 코드/메시지, 리소스 사용.)
    public <T> ResourceResponse<T> setSuccessResponse(String url, T resource) {
        ResourceResponse<T> response = new ResourceResponse<>(url, true);
        response.setResponseCode(ResponseType.SUCCESS.getCode());
        response.setResponseMessage(ResponseType.SUCCESS.getMessage());
        response.setResponseResource(resource);

        return response;
    }

    // 성공 응답. (커스텀 코드/메시지, 리소스 사용.)
    public <T> ResourceResponse<T> setSuccessResponse(String url, String code, String message, T resource) {
        ResourceResponse<T> response = new ResourceResponse<>(url, true);
        response.setResponseCode(code);
        response.setResponseMessage(message);
        response.setResponseResource(resource);

        return response;
    }

    // ...

    // 실패 응답. (기본 코드/메시지 사용.)
    public CommonResponse setFailureResponse(String url) {
        CommonResponse response = new CommonResponse(url, false);
        response.setResponseCode(ResponseType.FAILURE.getCode());
        response.setResponseMessage(ResponseType.FAILURE.getMessage());

        return response;
    }

    // 실패 응답. (커스텀 코드/메시지 사용.)
    public CommonResponse setFailureResponse(String url, String code, String message) {
        CommonResponse response = new CommonResponse(url, false);
        response.setResponseCode(code);
        response.setResponseMessage(message);

        return response;
    }

    // 실패 응답. (기본 코드/메시지, 리소스 사용.)
    public <T> ResourceResponse<T> setFailureResponse(String url, T resource) {
        ResourceResponse<T> response = new ResourceResponse<>(url, false);
        response.setResponseCode(ResponseType.FAILURE.getCode());
        response.setResponseMessage(ResponseType.FAILURE.getMessage());
        response.setResponseResource(resource);

        return response;
    }

    // 실패 응답. (커스텀 코드/메시지, 리소스 사용.)
    public <T> ResourceResponse<T> setFailureResponse(String url, String code, String message, T resource) {
        ResourceResponse<T> response = new ResourceResponse<>(url, false);
        response.setResponseCode(code);
        response.setResponseMessage(message);
        response.setResponseResource(resource);

        return response;
    }
}