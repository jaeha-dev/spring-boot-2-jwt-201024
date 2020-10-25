package com.github.devsjh.application.web;

import com.github.devsjh.application.aop.exception.UsernameExistedException;
import com.github.devsjh.application.security.CurrentUser;
import com.github.devsjh.application.security.CustomUserDetails;
import com.github.devsjh.application.utility.CookieUtils;
import com.github.devsjh.application.utility.JwtUtils;
import com.github.devsjh.domain.model.User;
import com.github.devsjh.domain.payload.common.UserToken;
import com.github.devsjh.domain.payload.request.JoinRequest;
import com.github.devsjh.domain.payload.request.LoginRequest;
import com.github.devsjh.domain.payload.response.ResourceResponse;
import com.github.devsjh.domain.service.UserCommonResponseService;
import com.github.devsjh.domain.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.Locale;

/**
 * @memo: 계정 컨트롤러 클래스.
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;
    // private final ResponseService responseService;
    private final UserCommonResponseService userResponseService;
    private final CookieUtils cookieUtils;

    @PostMapping("/join")
    public ResourceResponse<?> register(@Valid @RequestBody JoinRequest joinRequest,
                                        HttpServletRequest request) {
        // 계정 ID, 이메일 중복 검사.
        if (userService.existsByUsername(joinRequest.getUsername())) {
            throw new UsernameExistedException();
        }

        User createdUser = userService.createUser(joinRequest); // 계정 생성.

        return userResponseService.setUserResponse(request, UserCommonResponseService.UserResponseType.REGISTER, createdUser);
        // return responseService.setSuccessResponse(request.getRequestURL().toString());
    }

    @PostMapping("/login")
    public ResourceResponse<?> login(@Valid @RequestBody LoginRequest loginRequest,
                                     HttpServletRequest request,
                                     HttpServletResponse response) {
        // 계정 인증.
        userService.loginUser(loginRequest);

        // JWT 생성, 토큰을 응답의 쿠키에 담아 보낸다.
        UserToken userToken = userService.createToken(loginRequest.getUsername());
        response.addCookie(userToken.getAccessToken());
        response.addCookie(userToken.getRefreshToken());

        return userResponseService.setUserResponse(request, UserCommonResponseService.UserResponseType.LOGIN, userToken);
    }

    @PostMapping("/logout")
    public ResourceResponse<?> logout(HttpServletRequest request,
                                      HttpServletResponse response) {

        // 요청으로 전달된 토큰을 모두 받는다.
        Cookie accessToken = cookieUtils.getCookie(request, JwtUtils.ACCESS_TOKEN_NAME);
        Cookie refreshToken = cookieUtils.getCookie(request, JwtUtils.REFRESH_TOKEN_NAME);
        UserToken userToken = userService.logoutUser(accessToken, refreshToken);

        // 빈 토큰을 응답의 쿠키에 담아 보낸다.
        response.addCookie(userToken.getAccessToken());
        response.addCookie(userToken.getRefreshToken());

        return userResponseService.setUserResponse(request, UserCommonResponseService.UserResponseType.LOGOUT, userToken);
    }

    @GetMapping("/all") // 전체 허용 검사.
    public ResourceResponse<?> allAccess(HttpServletRequest request) {
        return userResponseService.setUserResponse(request, UserCommonResponseService.UserResponseType.COMMON, "Public Content");
    }

    @PreAuthorize("isAuthenticated()") // 인증 여부 검사.
    @GetMapping("/authenticated")
    public ResourceResponse<?> authenticatedAccess(HttpServletRequest request) {
        return userResponseService.setUserResponse(request, UserCommonResponseService.UserResponseType.COMMON, "Authenticated Content");
    }

    @PreAuthorize("hasRole('ROLE_USER')") // ROLE_ADMIN 권한 검사.
    @GetMapping("/user")
    public ResourceResponse<?> userAccess(@CurrentUser CustomUserDetails currentUser,
                                          HttpServletRequest request) {
        showUserDetails(currentUser);

        return userResponseService.setUserResponse(request, UserCommonResponseService.UserResponseType.COMMON, "User Content");
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')") // ROLE_ADMIN 권한 검사.
    @GetMapping("/admin")
    public ResourceResponse<?> adminAccess(@CurrentUser CustomUserDetails currentUser,
                                           HttpServletRequest request) {
        showUserDetails(currentUser);

        return userResponseService.setUserResponse(request, UserCommonResponseService.UserResponseType.COMMON, "Admin Content");
    }

    // 현재 컨텍스트에 등록된 계정 정보 출력.
    private void showUserDetails(CustomUserDetails currentUser) {
        log.info("Username: {}", currentUser.getUsername());
        log.info("Password: {}", currentUser.getPassword());
        log.info("Roles: {}", currentUser.getAuthorities());
    }
}