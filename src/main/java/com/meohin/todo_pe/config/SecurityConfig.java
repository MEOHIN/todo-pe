package com.meohin.todo_pe.config;

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
        // 항상 명시적으로 코딩.
        // 규칙 : 모두 비허용 상태에서, 하나씩 개방.
        // 백엔드는 보안이 생명, 모든 요청이 가능하다는 상황에서 처리해야함.
        // 보안 정책 : Security Policy
        //  1. 요청수신
        //      - 사용자가 웹 브라우저를 통해 "timetrainer.net/"에 대한 요청을 보냄.
        //      - 이 요청은 먼저 서버에 도달하고, Spring Boot 내장 톰캣 서버가 이를 수신.
        //  2. DispatcherServlet
        //      - 요청은 DispatcherServlet으로 전달.
        //  3. Spring Security 필터 체인
        //      - 요청은 Spring Security 필터 체인을 거침
        //          - SecurityContextPersistenceFilter: SecurityContext를 로드하고 요청이 끝날 때까지 유지. 이는 사용자의 인증 정보를 담고 있음.
        //          - UsernamePasswordAuthenticationFilter: 로그인 요청을 처리.
        //          - FilterSecurityInterceptor: 접근 제어 결정을 내리고, 요청이 허용되는지 여부를 결정.
        //  4. 인증 및 권한 부여
        //      - 사용자가 인증되지 않은 상태에서 "timetrainer.net/"에 접근하려고 하면, FilterSecurityInterceptor는 이를 감지하고 사용자가 인증되어야 한다는 결정을 내림
        //      - 사용자가 인증되지 않았기 때문에, ExceptionTranslationFilter가 작동하여 AuthenticationException을 캐치하고, 사용자를 로그인 페이지로 리다이렉션.
        //  5. 로그인 페이지 리다이렉션
        //      - LoginUrlAuthenticationEntryPoint는 사용자를 로그인 페이지로 리다이렉션하는 역할. 이는 httpSecurity.formLogin().loginPage("/login") 설정에 의해 정의됨.
        //      - 용자는 이제 로그인 페이지에 도달하게 되며, 자신의 자격 증명을 입력할 수 있음.
        //  6. 로그인 요청 처리
        //      - 사용자가 로그인 정보를 입력하고 제출하면, UsernamePasswordAuthenticationFilter가 이 요청을 처리.
        //      - 사용자의 자격 증명은 AuthenticationManager를 통해 검증
        //      - 인증이 성공하면, SecurityContext에 사용자의 인증 정보가 저장되고, 사용자는 원래 요청했던 리소스나 기본 페이지로 리다이렉션.
        httpSecurity.authorizeHttpRequests((authorizeHttpRequests) -> {
            authorizeHttpRequests.requestMatchers("/login").permitAll();
            authorizeHttpRequests.requestMatchers("/style.css").permitAll();
            authorizeHttpRequests.anyRequest().authenticated();
        });

        // 로그인 및 로그아웃
        // 로그인 실패 설정 옵션1) .formLogin((formLogin) -> formLogin.loginPage("/login")
        //                    .failureUrl("/login?error=true") // 로그인 실패 시 리다이렉션할 URL 설정
        //                    .permitAll());
        // 로그인 실패 설정 옵션2) .formLogin((formLogin) -> formLogin.loginPage("/login")
        //                    .defaultSuccessUrl("/"));
        // 로그인 실패 설정 옵션1은 로그인 성공 시 사용자를 어디로 리다이렉션할지 명시적으로 지정하지 않음.
        // 따라서 옵션1 코드는 로그인 성공 후의 리다이렉션은 사용자가 처음 요청한 페이지에 따라 달라짐.
        // 반면, defaultSuccessUrl("/")를 사용하는 코드는 로그인 성공 시 항상 지정된 URL로 리다이렉션하도록 설정
        // https://docs.spring.io/spring-security/site/docs/6.0.3/api/
        // https://docs.spring.io/spring-security/site/docs/6.0.3/api/org/springframework/security/config/annotation/web/builders/HttpSecurity.html#logout(org.springframework.security.config.Customizer)
        // 항상 API 문설를 확인하도록 하자.
        httpSecurity.formLogin((formLogin) -> formLogin.loginPage("/login"));
        httpSecurity.logout((logout) -> logout.logoutRequestMatcher(new AntPathRequestMatcher("/logout")));

        return httpSecurity.build();
    }
}
