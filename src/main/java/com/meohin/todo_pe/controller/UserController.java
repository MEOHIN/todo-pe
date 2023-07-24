package com.meohin.todo_pe.controller;

import com.meohin.todo_pe.dto.UserDTO;
import com.meohin.todo_pe.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequiredArgsConstructor
@Controller
public class UserController {

    private final UserService userService;

    @RequestMapping("/login")
    public String login() {
        return "/login/login";
    }

    @GetMapping("/signup")
    public String signup(UserDTO userDTO) {
        return "login/signup_form";
    }

    @PostMapping("/signup")
    public String signup(@Valid UserDTO userDTO, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return "login/signup_form";
        }

        if (!userDTO.getPw1().equals(userDTO.getPw2())) {
            bindingResult.rejectValue("pw2", "passwordInCorrect", "입력한 2개의 패스워드가 일치하지 않습니다.");
            return "/login/signup_form";
        }

        this.userService.saveUserInfo(userDTO.getUserId(), userDTO.getPw1(), userDTO.getEmail());
        return "redirect:/task/list";
    }
}
