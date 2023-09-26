package com.meohin.todo_pe.validationObject;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
/**
 * 예상 처리 시간 입력값 검증 클래스
 */
public class EstimatedTimeVO {
    @NotEmpty(message = "예상 처리 시간은 필수 입력 항목입니다.")
    @Pattern(regexp = "^\\d{2,}:[0-5]\\d$", message = "예상 처리 시간은 00:00 형식으로 입력해주세요.\n예상 처리 시간이 3시간 30분이라면 03:30으로 입력합니다.")
    private String inputEstimatedAt;
}
