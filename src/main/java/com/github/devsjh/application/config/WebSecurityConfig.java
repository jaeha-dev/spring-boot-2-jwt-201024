package com.github.devsjh.application.config;

import com.github.devsjh.application.security.CustomAccessDeniedHandler;
import com.github.devsjh.application.security.CustomAuthenticationEntryPoint;
import com.github.devsjh.application.security.CustomUserDetailsService;
import com.github.devsjh.application.security.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * @memo: 웹 보안 설정 클래스.
 */
@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity( // 메소드 레벨의 보안 설정 활성화.
        // securedEnabled = true, // @Secured.
        // jsr250Enabled = true, // @RolesAllowed.
        prePostEnabled = true // @PreAuthorize, @PostAuthorize.
)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final CustomUserDetailsService customUserDetailsService;
    private final CustomAuthenticationEntryPoint authenticationEntryPoint;
    private final CustomAccessDeniedHandler accessDeniedHandler;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Override
    public void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
        // 인증 수행을 위한 빌더 객체를 지정한다. (In-memory, LDAP, JDBC 등 여러 인증 방법이 있다.)
        authenticationManagerBuilder
                .userDetailsService(customUserDetailsService)
                .passwordEncoder(passwordEncoder()); // 비밀번호 비교를 위한 객체 지정.
    }

    @Bean(BeanIds.AUTHENTICATION_MANAGER)
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        // 비밀번호 암호화를 수행하는 빈 등록
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .httpBasic()
                .disable()
            .formLogin()
                .disable()
            .logout()
                .disable()
            .csrf()
                .disable()
            .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // RESTful은 무상태성을 갖는다.
                .and()
            .exceptionHandling()
                .authenticationEntryPoint(authenticationEntryPoint) // 미인증 사용자에 대한 엔트리 포인트.
                .and()
            .exceptionHandling()
                .accessDeniedHandler(accessDeniedHandler) // 비인가 사용자에 대한 핸들러.
                .and()
            .authorizeRequests()
                .antMatchers("/", // 리소스 접근 권한. (All)
                        "/favicon.ico",
                        "/**/*.png",
                        "/**/*.gif",
                        "/**/*.svg",
                        "/**/*.jpg",
                        "/**/*.html",
                        "/**/*.css",
                        "/**/*.js")
                .permitAll()
            .antMatchers("/web/i18n") // i18n 테스트 페이지 접근 권한. (All)
                .permitAll()
            .antMatchers("/api/user/join") // 계정 등록 접근 권한. (All)
                .permitAll()
            .antMatchers("/api/user/login") // 계정 로그인 접근 권한. (All)
                .permitAll()
            .antMatchers("/api/user/logout") // 계정 로그아웃 접근 권한. (All)
                .permitAll()
            .antMatchers("/api/user/all") // 전체 허용 접근 권한. (All)
                .permitAll()
            .antMatchers(HttpMethod.GET, "/error/**") // 에러 경로 접근 권한 예외. (All)
                .permitAll()
            .anyRequest()
                .authenticated(); // 이외는 모두 인증을 요구한다.

        // UsernamePasswordAuthenticationFilter 이전 필터에 JWT 커스텀 필터를 지정한다.
        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
    }
}



//package com.github.devsjh.application.config;
//
//import com.github.devsjh.application.security.CustomAccessDeniedHandler;
//import com.github.devsjh.application.security.CustomAuthenticationEntryPoint;
//import com.github.devsjh.application.security.CustomUserDetailsService;
//import com.github.devsjh.application.security.JwtAuthenticationFilter;
//import lombok.RequiredArgsConstructor;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.http.HttpMethod;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.config.BeanIds;
//import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
//import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
//import org.springframework.security.config.http.SessionCreationPolicy;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
//
///**
// * @memo: 웹 보안 설정 클래스.
// */
//@RequiredArgsConstructor
//@Configuration
//@EnableWebSecurity
//@EnableGlobalMethodSecurity( // 메소드 레벨의 보안 설정 활성화.
//        // securedEnabled = true, // @Secured.
//        // jsr250Enabled = true, // @RolesAllowed.
//        prePostEnabled = true // @PreAuthorize, @PostAuthorize.
//)
//public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
//
//    private final CustomUserDetailsService customUserDetailsService;
//    private final CustomAuthenticationEntryPoint authenticationEntryPoint;
//    private final CustomAccessDeniedHandler accessDeniedHandler;
//    private final JwtAuthenticationFilter jwtAuthenticationFilter;
//
//    @Override
//    public void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
//        // 인증 수행을 위한 빌더 객체를 지정한다. (In-memory, LDAP, JDBC 등 여러 인증 방법이 있다.)
//        authenticationManagerBuilder
//                .userDetailsService(customUserDetailsService)
//                .passwordEncoder(passwordEncoder()); // 비밀번호 비교를 위한 객체 지정.
//    }
//
//    @Bean(BeanIds.AUTHENTICATION_MANAGER)
//    @Override
//    public AuthenticationManager authenticationManagerBean() throws Exception {
//        return super.authenticationManagerBean();
//    }
//
//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        // 비밀번호 암호화를 수행하는 빈 등록
//        return new BCryptPasswordEncoder();
//    }
//
//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//        http
//            // .cors()
//            //     .and()
//            .csrf()
//                .disable()
//            .sessionManagement()
//                .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // RESTful은 무상태성을 갖는다.
//                .and()
//            .httpBasic()
//                .authenticationEntryPoint(authenticationEntryPoint) // 미인증 사용자에 대한 엔트리 포인트.
//                .and()
//            // .exceptionHandling()
//            //     .authenticationEntryPoint(authenticationEntryPoint)
//            //     .and()
//            .exceptionHandling()
//                .accessDeniedHandler(accessDeniedHandler) // 비인가 사용자에 대한 핸들러.
//                .and()
//            .authorizeRequests()
//                .antMatchers("/", // 리소스 접근 권한 (All)
//                        "/favicon.ico",
//                        "/**/*.png",
//                        "/**/*.gif",
//                        "/**/*.svg",
//                        "/**/*.jpg",
//                        "/**/*.html",
//                        "/**/*.css",
//                        "/**/*.js")
//                .permitAll()
//            .antMatchers("/api/auth/**") // 인증 접근 권한 (All)
//                .permitAll()
//            .antMatchers(HttpMethod.GET, "/error/**") // 인증/접근 권한 예외 (All)
//                .permitAll()
//            .anyRequest()
//                .authenticated(); // 이외는 모두 인증을 요구한다.
//
//        // UsernamePasswordAuthenticationFilter 이전 필터에 JWT 커스텀 필터를 지정한다.
//        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
//    }
//}