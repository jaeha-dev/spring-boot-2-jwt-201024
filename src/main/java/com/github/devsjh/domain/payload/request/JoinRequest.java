package com.github.devsjh.domain.payload.request;

import lombok.Getter;
import lombok.Setter;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * @memo: 계정 등록 요청 페이로드 클래스.
 */
@Getter
@Setter
public class JoinRequest {

    @NotBlank(message = "{user.username.empty}")
    @Size(min = 5, max = 15, message = "{user.username.incorrect}")
    private String username;

    @NotBlank(message = "{user.password.empty}")
    @Size(min = 10, max = 20, message = "{user.password.incorrect}")
    private String password;
}