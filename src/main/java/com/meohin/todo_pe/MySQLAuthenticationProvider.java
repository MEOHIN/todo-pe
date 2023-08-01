package com.meohin.todo_pe;

import com.meohin.todo_pe.service.UserSecurityService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * 스프링 시큐리티에서 인증정보가 올바른지 체크하도록 일임하는 Provider 구현
 */
@RequiredArgsConstructor
@Component // Spring security 가 커스텀 프로바이더를 찾도록 스프링 빈으로 등록한다.
public class MySQLAuthenticationProvider implements AuthenticationProvider {

    private final UserSecurityService userSecurityService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        // 입력받은 ID와 비밀번호를 변수에 할당
        String userId = authentication.getName();
        String pw = (String) authentication.getCredentials();

        // 데이터베이스에 저장된 회원 정보를 입력받은 ID로 조회
        UserDetails mySQLUser = this.userSecurityService.loadUserByUsername(userId);

        // passwordEncoder의 matches() 메서드를 사용해서 입력받은 비밀번호(암호화되지 않은 비밀번호)와 저장된 비밀번호(암호화된 비밀번호)를 비교
        if (!passwordEncoder.matches(pw, mySQLUser.getPassword())) {
            // 만약에 입력받은 비밀번호와 저장된 비밀번호가 다르면 예외 발생
            throw new BadCredentialsException(authentication.getName());
        }

        // 입력받은 ID, 비밀번호, 로그인한 사용자에게 부여할 권한을 담아서 UsernamePasswordAuthenticationToken 생성
        // UsernamePasswordAuthenticationToken 객체는 곧 Authentication 객체다.
        return new UsernamePasswordAuthenticationToken(userId, pw, mySQLUser.getAuthorities());
    }

    private boolean matchPassword(String loginPwd, String password) {
        return loginPwd.equals(password);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        // 주어진 authentication 파라미터가 UsernamePasswordAuthenticationToken 클래스 또는 그 하위 클래스인지 확인
        // UsernamePasswordAuthenticationToken 클래스 또는 그 하위 클래스인 경우에만 true를 반환하여 해당 토큰이 이 AuthenticationProvider에서 지원되도록 설정
        return (UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication));
    }
}
