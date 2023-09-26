package com.meohin.todo_pe.controller;

import com.meohin.todo_pe.validationObject.UserVO;
import com.meohin.todo_pe.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.security.Principal;

@RequiredArgsConstructor
@Controller
public class UserController {

    private final UserService userService;

    @GetMapping("/login")
    public String login(Principal principal) {
        if (principal != null) {
            return "redirect:/task/list";
        }
        return "/login/login";
    }

    @GetMapping("/signup")
    public String signup(UserVO userVO, Principal principal) {
        if (principal != null) {
            return "redirect:/task/list";
        }
        return "login/signup_form";
    }

    @PostMapping("/signup")
    public String signup(@Valid UserVO userVO, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return "login/signup_form";
        }

        if (!userVO.getInputPW().equals(userVO.getConfirmPW())) {
            bindingResult.rejectValue("pw2", "passwordInCorrect", "입력한 2개의 패스워드가 일치하지 않습니다.");
            return "/login/signup_form";
        }

        try {
            this.userService.saveUserInfo(userVO.getInputUserId(), userVO.getInputPW(), userVO.getInputEmail());
        } catch (DataIntegrityViolationException e) {
            e.printStackTrace();
            bindingResult.reject("signupFailed", "이미 가입된 회원입니다.");
            return "/login/signup_form";
        } catch (Exception e) {
            e.printStackTrace();
            bindingResult.reject("signupFailed", e.getMessage());
            return "/login/signup_form";
        }
        return "redirect:/task/list";
    }
}
