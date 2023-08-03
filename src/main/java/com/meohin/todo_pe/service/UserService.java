package com.meohin.todo_pe.service;

import com.meohin.todo_pe.entity.SiteUser;
import com.meohin.todo_pe.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public SiteUser saveUserInfo(String userId, String pw, String email) {
        SiteUser siteUser = new SiteUser();
        siteUser.setUserId(userId);
        siteUser.setPassword(passwordEncoder.encode(pw));
        siteUser.setEmail(email);
        userRepository.save(siteUser);
        return siteUser;
    }

    public SiteUser getUser(String username) {
        Optional<SiteUser> user = this.userRepository.findByUserId(username);

        if (user.isPresent()) {
            return user.get();
        } else {
            throw new RuntimeException("User ID not found");
        }
    }
}
