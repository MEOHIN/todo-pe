package com.meohin.todo_pe.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDTO {

    @Pattern(regexp = "^[a-z0-9_-]{5,20}$", message = "아이디는 5~20자의 영문 소문자, 숫자와 특수기호(_),(-)만 사용 가능합니다.")
    @NotEmpty(message = "ID는 필수 입력 항목입니다.")
    private String userId;

    @Pattern(regexp = "^[a-zA-Z0-9!@#$%^&*()_-]{8,16}$", message = "비밀번호는 8~16자의 영문 대/소문자, 숫자, 특수문자를 사용해주세요.")
    @NotEmpty(message = "비밀번호는 필수 입력 항목입니다.")
    private String pw1;

    @NotEmpty(message = "비밀번호 확인은 필수 입력 항목입니다.")
    private String pw2;

    @NotEmpty(message = "이메일은 필수 입력 항목입니다.")
    @Email
    private String email;

}
