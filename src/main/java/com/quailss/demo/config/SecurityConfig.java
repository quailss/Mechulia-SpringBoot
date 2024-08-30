package com.quailss.demo.config;

import com.quailss.demo.service.CustomOAuth2UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {
    @Autowired private CustomOAuth2UserService customOAuth2UserService;
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeRequests(authorize -> authorize
                        .requestMatchers("/", "/home", "/login", "/swagger-ui/**", "/v3/api-docs/**").permitAll()
                        .anyRequest().authenticated()
                )
                .formLogin(formLogin ->
                        formLogin
                                .loginPage("/login")  // 로그인 페이지 경로 설정
                                .defaultSuccessUrl("/home", true)  // 로그인 성공 후 리디렉션할 URL 설정
                                .permitAll()  // 로그인 페이지는 누구나 접근 가능
                )
                .logout(logout ->
                        logout
                                .logoutSuccessUrl("/login?logout")  // 로그아웃 성공 후 리디렉션할 URL
                                .permitAll()  // 로그아웃 경로는 누구나 접근 가능
                )
                .csrf(csrf -> csrf.disable());  // 필요에 따라 CSRF 비활성화 (개발 중이거나 특정 API 사용 시)
        return http.build();
    }

    @Bean
    public OAuth2UserService<OAuth2UserRequest, OAuth2User> oAuth2UserService() {
        DefaultOAuth2UserService delegate = new DefaultOAuth2UserService();
        return request -> {
            OAuth2User oAuth2User = delegate.loadUser(request);
            // 카카오, 네이버 OAuth2 유저 정보 로드 및 추가 처리
            return oAuth2User;
        };
    }
}
