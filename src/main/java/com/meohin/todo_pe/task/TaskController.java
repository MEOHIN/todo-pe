package com.meohin.todo_pe.task;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@RequiredArgsConstructor    // TaskController 객체가 생성될 때 TaskRepository가 주입되도록 한다.
@Controller
public class TaskController {

    private final TaskService taskService;    // TaskRepository를 사용하여 작업 목록을 조회할 수 있도록 한다.

    @RequestMapping("/task/list")
    public String list(Model model) {
        List<TaskDTO> taskDTOList = taskService.getTaskList();    // 모든 작업(Task)을 조회한다.
        model.addAttribute("taskList", taskDTOList);   // 조회된 작업 목록을 Model 객체에 추가한다.
        return "task_list";
    }
}
