package com.meohin.todo_pe.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TaskDTO {

    @NotEmpty(message = "제목은 필수 입력 항목입니다.")
    private String subject;

    @NotEmpty(message = "내용은 필수 입력 항목입니다.")
    private String description;

    @NotEmpty(message = "예상 처리 시간은 필수 입력 항목입니다.")
    @Pattern(regexp = "^(?:[01]\\d|2[0-3]):[0-5]\\d$", message = "예상 처리 시간은 00:00 형식으로 입력해주세요.\n예상 처리 시간이 3시간 30분이라면 3:30으로 입력합니다.")
    private String estimatedAt;
}
