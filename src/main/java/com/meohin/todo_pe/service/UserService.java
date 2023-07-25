package com.meohin.todo_pe.service;

import com.meohin.todo_pe.entity.User;
import com.meohin.todo_pe.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;

    public User saveUserInfo(String userId, String pw, String email) {
        User user = new User();
        user.setUserId(userId);
        user.setEmail(email);
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        user.setPassword(pw);
        userRepository.save(user);
        return user;
    }
}
