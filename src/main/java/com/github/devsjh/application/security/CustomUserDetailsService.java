package com.github.devsjh.application.security;

import com.github.devsjh.application.aop.exception.ResourceNotFoundException;
import com.github.devsjh.domain.model.User;
import com.github.devsjh.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @memo: DB에서 계정 정보를 조회하기 위한 클래스.
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws ResourceNotFoundException {
        User user = userRepository.findByUsername(username)
                // 계정 정보가 존재한다면 계정 객체를 반환하고
                // 계정 정보가 존재하지 않는다면 인수(Argument)로 지정한 예외를 발생시킨다.
                .orElseThrow(ResourceNotFoundException::new);

        return CustomUserDetails.create(user);
    }
}