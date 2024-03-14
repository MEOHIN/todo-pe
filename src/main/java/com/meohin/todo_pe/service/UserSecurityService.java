package com.meohin.todo_pe.service;

import com.meohin.todo_pe.entity.UserRole;
import com.meohin.todo_pe.entity.SiteUser;
import com.meohin.todo_pe.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserSecurityService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        // 아이디가 존재하는지 검사
        Optional<SiteUser> _siteUser = this.userRepository.findByUserId(userId);
        System.out.println(userId);
        if (_siteUser.isEmpty()) {
            throw new UsernameNotFoundException("아이디 또는 비밀번호가 일치하지 않습니다.");
        }

        // 로그인한 사용자에게 해당하는 권한을 부여함. (추후 권한별 접근제어를 위해 활용)
        SiteUser siteUser = _siteUser.get();
        List<GrantedAuthority> authorities = new ArrayList<>();
        if ("admin".equals(userId)) {
            authorities.add(new SimpleGrantedAuthority(UserRole.ADMIN.getValue()));
        } else {
            authorities.add(new SimpleGrantedAuthority(UserRole.USER.getValue()));
        }

        //  Spring Security 프레임워크에서 제공하는 User 클래스는 Spring Security에서 사용자 정보를 캡슐화하고, 인증 및 권한 확인 등을 수행
        return new User(siteUser.getUserId(), siteUser.getPassword(), authorities);
    }
}
