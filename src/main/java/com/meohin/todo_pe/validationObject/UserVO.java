package com.meohin.todo_pe.validationObject;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
/**
 * 사용자 정보 입력값 검증 클래스
 */
public class UserVO {

    @Pattern(regexp = "^[a-z0-9_-]{5,20}$", message = "아이디는 5~20자의 영문 소문자, 숫자와 특수기호(_),(-)만 사용 가능합니다.")
    @NotEmpty(message = "ID는 필수 입력 항목입니다.")
    private String inputUserId;

    @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[\\W_]).{8,16}$", message = "비밀번호는 영문 대/소문자, 숫자, 특수문자를 각 하나 이상 포함하여 8~16자를 입력합니다.")
    @NotEmpty(message = "비밀번호는 필수 입력 항목입니다.")
    private String inputPW;

    @NotEmpty(message = "비밀번호 확인은 필수 입력 항목입니다.")
    private String confirmPW;

    @NotEmpty(message = "이메일은 필수 입력 항목입니다.")
    @Email
    private String inputEmail;

}
