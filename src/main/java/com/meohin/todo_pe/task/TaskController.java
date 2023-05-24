package com.meohin.todo_pe.task;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RequiredArgsConstructor    // TaskController 객체가 생성될 때 TaskRepository가 주입되도록 한다.
@Controller
public class TaskController {

    private final TaskRepository taskRepository;    // TaskRepository를 사용하여 작업 목록을 조회할 수 있도록 한다.

    @RequestMapping("/task/list")
    public String list() {
        return "task_list";
    }
}
