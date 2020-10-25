package com.github.devsjh.domain.service;

import com.github.devsjh.domain.payload.response.ResourceResponse;
import org.springframework.stereotype.Service;
import javax.servlet.http.HttpServletRequest;
import static com.github.devsjh.application.utility.MessageSourceUtils.getCode;
import static com.github.devsjh.application.utility.MessageSourceUtils.getMessage;

/**
 * @memo: 응답 서비스 클래스.
 */
@Service
public class UserCommonResponseService extends CommonResponseService {

    public enum UserResponseType {
        COMMON("server.request.code", "server.request.message"),
        REGISTER("user.register.code", "user.register.message"),
        LOGIN("user.login.code", "user.login.message"),
        LOGOUT("user.logout.code", "user.logout.message");

        private final String code;
        private final String message;

        UserResponseType(String code, String message) {
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

    public ResourceResponse<?> setUserResponse(HttpServletRequest request, UserResponseType type, Object object) {
        return super.setSuccessResponse(
                request.getRequestURL().toString(),
                getCode(type.getCode()),
                getMessage(type.getMessage()),
                object
        );
    }
}