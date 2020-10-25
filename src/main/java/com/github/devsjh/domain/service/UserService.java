package com.github.devsjh.domain.service;

import com.github.devsjh.application.aop.exception.LoginFailedException;
import com.github.devsjh.application.aop.exception.LogoutFailedException;
import com.github.devsjh.application.aop.exception.ResourceNotFoundException;
import com.github.devsjh.application.utility.CookieUtils;
import com.github.devsjh.application.utility.JwtUtils;
import com.github.devsjh.application.utility.RedisUtils;
import com.github.devsjh.domain.model.Role;
import com.github.devsjh.domain.model.RoleType;
import com.github.devsjh.domain.model.User;
import com.github.devsjh.domain.payload.common.UserToken;
import com.github.devsjh.domain.payload.request.JoinRequest;
import com.github.devsjh.domain.payload.request.LoginRequest;
import com.github.devsjh.domain.repository.RoleRepository;
import com.github.devsjh.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import javax.servlet.http.Cookie;
import java.util.Collections;

/**
 * @memo: 계정 서비스 클래스.
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    private final CookieUtils cookieUtils;
    private final RedisUtils redisUtils;

    // 계정 ID 중복 검사.
    public Boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    // 계정 생성.
    public User createUser(JoinRequest joinRequest) throws ResourceNotFoundException {
        Role role = roleRepository.findByName(RoleType.ROLE_USER)
                .orElseThrow(ResourceNotFoundException::new);

        User user = User.builder()
                .username(joinRequest.getUsername())
                .password(passwordEncoder.encode(joinRequest.getPassword()))
                .roles(Collections.singleton(role))
                .build();

        return userRepository.save(user);
    }

    // 계정 인증 및 스프링 컨텍스트 등록.
    public void loginUser(LoginRequest loginRequest) throws LoginFailedException {
        try {
            // 인증 객체 생성. (authenticate() 메소드 호출 과정에 비밀번호 비교를 포함된다.)
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getUsername(),
                            loginRequest.getPassword()
                    )
            );

            // 스프링 컨텍스트에 인증 정보 등록.
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } catch (Exception e) {
            // AuthenticationException.
            throw new LoginFailedException();
        }
    }

    // 액세스, 리프레시 토큰 생성.
    public UserToken createToken(String username) throws LoginFailedException {
        try {
            // 액세스, 리프레시 JWT를 생성한다.
            final String accessJwt = jwtUtils.generateAccessToken(username);
            final String refreshJwt = jwtUtils.generateRefreshToken(username);

            // 쿠키에 토큰을 저장하므로 쿠키를 생성한다.
            Cookie accessToken = cookieUtils.createCookie(JwtUtils.ACCESS_TOKEN_NAME, accessJwt);
            Cookie refreshToken = cookieUtils.createCookie(JwtUtils.REFRESH_TOKEN_NAME, refreshJwt);

            // Redis에 리프레시 토큰과 유효 시간을 저장한다.
            redisUtils.setDataExpire(refreshJwt, username, JwtUtils.REFRESH_TOKEN_VALIDATION_MILLISECOND);

            return new UserToken(accessToken, refreshToken);
        } catch (Exception e) {
            throw new LoginFailedException();
        }
    }

    // 계정 로그아웃을 위한 Redis 작업과 빈 토큰 생성.
    // 토큰 검사 없이 try ~ catch문을 사용하여 해당 토큰 값이 없을 때 로그아웃 실패를 유도함.
    public UserToken logoutUser(Cookie accessToken, Cookie refreshToken) throws LogoutFailedException {
        try {
            // Redis에서 리프레시 토큰을 지운다.
            redisUtils.deleteData(refreshToken.getValue());

            // 빈 쿠키를 만든다.
            accessToken = cookieUtils.createCookie(JwtUtils.ACCESS_TOKEN_NAME, "");
            refreshToken = cookieUtils.createCookie(JwtUtils.REFRESH_TOKEN_NAME, "");

            // 쿠키의 유효 시간을 0초로 만든다. (무효화)
            accessToken.setMaxAge(0);
            refreshToken.setMaxAge(0);

            return new UserToken(accessToken, refreshToken);
        } catch (Exception e) {
            throw new LogoutFailedException();
        }
    }
}