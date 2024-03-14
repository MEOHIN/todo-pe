package com.meohin.todo_pe;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.authorizeHttpRequests((authorizeHttpRequests) -> {
            authorizeHttpRequests.requestMatchers(new AntPathRequestMatcher("/login")).permitAll();
            authorizeHttpRequests.requestMatchers(new AntPathRequestMatcher("/style.css")).permitAll();
            authorizeHttpRequests.anyRequest().authenticated();
        });

        // 로그인 및 로그아웃
        httpSecurity.formLogin((formLogin) -> formLogin.loginPage("/login"));
        httpSecurity.logout((logout) -> logout.logoutRequestMatcher(new AntPathRequestMatcher("/logout")));

        return httpSecurity.build();
    }
}
