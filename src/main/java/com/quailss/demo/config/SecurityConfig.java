package com.quailss.demo.config;

import com.quailss.demo.service.CustomOAuth2UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {
    private final CustomOAuth2UserService customOAuth2UserService;
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(withDefaults()) // 명시적으로 CORS 필터 추가
                .csrf(csrf -> csrf.disable()) // CSRF 비활성화
                .authorizeHttpRequests(authorizeRequests ->
                        authorizeRequests
                                .requestMatchers("/", "/login", "/oauth2/**", "/api/recipe/**").permitAll()
                                .anyRequest().authenticated()
                )
                .oauth2Login(oauth2Login ->
                        oauth2Login
                                .defaultSuccessUrl("/") // 로그인 성공 후 리다이렉트 URL
                                .userInfoEndpoint(userInfoEndpoint ->
                                        userInfoEndpoint.userService(customOAuth2UserService)
                                )
                ).formLogin(formLogin ->
                        formLogin
                                .loginPage("/login") // 로그인 페이지 URL
                                .defaultSuccessUrl("/") // 로그인 성공 후 이동할 URL
                                .permitAll()
                )
                .logout(logout ->
                        logout
                                .logoutSuccessUrl("/login?logout")
                                .permitAll()
                );

        return http.build();
    }
}
