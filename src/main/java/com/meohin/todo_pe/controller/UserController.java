package com.meohin.todo_pe.controller;

import com.meohin.todo_pe.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RequiredArgsConstructor
@Controller
public class UserController {

    private final UserService userService;

    @RequestMapping("/login")
    public String login() {
        return "/login/login";
    }

    @GetMapping("/signup")
    public String signup() {
        return "login/signup_form";
    }

    @PostMapping("/signup")
    public String signup(Model model, @RequestParam String userId, @RequestParam String pw1, @RequestParam String pw2, @RequestParam String email) {

        if (!pw1.equals(pw2)) {
            String errorMessage = "입력한 2개의 패스워드가 일치하지 않습니다.";
            model.addAttribute("errorMessage", errorMessage);
            return "/login/signup_form";
        }

        this.userService.saveUserInfo(userId, pw1, email);
        return "redirect/task/list";
    }
}
