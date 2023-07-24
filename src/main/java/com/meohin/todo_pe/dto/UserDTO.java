package com.meohin.todo_pe.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDTO {

    @NotEmpty(message = "ID는 필수 입력 항목입니다.")
    private String userId;

    @NotEmpty(message = "비밀번호는 필수 입력 항목입니다.")
    private String pw1;

    @NotEmpty(message = "비밀번호 확인은 필수 입력 항목입니다.")
    private String pw2;

    @NotEmpty(message = "이메일은 필수 입력 항목입니다.")
    @Email
    private String email;

}
