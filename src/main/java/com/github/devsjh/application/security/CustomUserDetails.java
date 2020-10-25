package com.github.devsjh.application.security;

import com.github.devsjh.domain.model.User;
import lombok.Builder;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @memo: 인증과 인가를 위해 사용할 계정 인증 정보 클래스.
 */
@Data
@Builder
public class CustomUserDetails implements UserDetails {

    private Long id;
    private String username;
    private String password;
    private Collection<? extends GrantedAuthority> authorities;

    // User 객체를 CustomUserDetails 객체로 변환한다.
    public static CustomUserDetails create(User user) {
        // 매개변수로 받은 User 객체의 계정 권한 집합(Set)을 꺼내어 스트림 객체를 생성한다.
        // 스트림 객체의 요소(role)들을 하나씩 가져와 SimpleGrantedAuthority 타입의 객체들을 생성하고 리스트로 묶는다.
        List<GrantedAuthority> authorities = user.getRoles()
                .stream()
                .map(role -> new SimpleGrantedAuthority(role.getName().name()))
                .collect(Collectors.toList());

        return new CustomUserDetails(user.getId(),
                user.getUsername(),
                user.getPassword(),
                authorities
        );
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public boolean isAccountNonExpired() {
        // 계정 만료 여부. (계정에 유효(만료) 기간을 지정할 경우에 사용한다.)
        return true; // true는 계정이 만료되지 않음을 의미한다.
    }

    @Override
    public boolean isAccountNonLocked() {
        // 계정 잠금 여부. (비밀번호를 여러 번 잘못 입력했을 때 계정을 잠그는 경우에 사용한다.)
        return true; // true는 계정이 잠겨있지 않음을 의미한다.
    }

    @Override
    public boolean isCredentialsNonExpired() {
        // 계정 비밀번호 만료 여부. (주기적으로 비밀번호를 변경해야 하는 경우에 사용한다.)
        return true; // true는 계정 비밀번호가 만료되지 않음을 의미한다.
    }

    @Override
    public boolean isEnabled() {
        // 계정 활성화 여부. (장기간 로그인을 하지 않은 계정을 비활성화 경우에 사용한다.)
        return true; // true는 활성화 상태인 계정을 의미한다.
    }
}