package com.meohin.todo_pe.controller;

import com.meohin.todo_pe.dao.TaskDTO;
import com.meohin.todo_pe.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * Task 컨틀롤러 클래스다.
 */
@RequiredArgsConstructor    // TaskController 객체가 생성될 때 TaskRepository가 주입되도록 한다.
@Controller
@RequestMapping("/task")    // 프리픽스 지정
public class TaskController {

    private final TaskService taskService;    // TaskRepository를 사용하여 작업 목록을 조회할 수 있도록 한다.

    /**
     * 모든 task를 조회하고, 조회된 목록을 Model 객체에 추가한다.
     * @param model     Model 객체
     * @return  Task 목록 템플릿
     */
    @RequestMapping("/list")
    public String list(Model model) {
        List<TaskDTO> taskDTOList = taskService.getTaskList();
        model.addAttribute("taskList", taskDTOList);
        return "task_list";
    }

    /**
     * 작업 상세 정보를 조회한다.
     * @param taskId    Task ID
     * @param model     Model 객체
     * @return  Task 상세 템플릿
     */
    @RequestMapping(value = "/detail/{taskId}")
    public String detail(@PathVariable("taskId") Long taskId, Model model) {
        TaskDTO taskDTO = taskService.getTaskById(taskId);
        model.addAttribute("task", taskDTO);
        return "task_detail";
    }

}
