package com.meohin.todo_pe.service;

import com.meohin.todo_pe.entity.User;
import com.meohin.todo_pe.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public User saveUserInfo(String userId, String pw, String email) {
        User user = new User();
        user.setUserId(userId);
        user.setPassword(passwordEncoder.encode(pw));
        user.setEmail(email);
        userRepository.save(user);
        return user;
    }
}
