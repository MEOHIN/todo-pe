package com.meohin.todo_pe.controller;

import com.meohin.todo_pe.dao.TaskDTO;
import com.meohin.todo_pe.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Task 컨틀롤러 클래스다.
 */
// TaskController 객체가 생성될 때 TaskRepository가 주입되도록 한다.
@RequiredArgsConstructor
@Controller
// 프리픽스를 지정한다.
@RequestMapping("/task")
public class TaskController {

    // TaskRepository를 사용하여 작업 목록을 조회할 수 있도록 한다.
    private final TaskService taskService;

    /**
     * 모든 task를 조회하고, 조회된 목록을 Model 객체에 추가한다.
     * @param model     Model 객체
     * @return  Task 목록
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
     * @return  Task 상세
     */
    @RequestMapping(value = "/detail/{taskId}")
    public String detail(@PathVariable("taskId") Long taskId, Model model) {
        TaskDTO taskDTO = taskService.getTaskById(taskId);
        model.addAttribute("task", taskDTO);
        return "task_detail";
    }

    /**
     * tast_form 템플릿을 렌더링하여 출력한다.
     * @return Task 입력
     */
    @GetMapping("/create")
    public String createTask() {
        return "task_form";
    }

    /**
     * POST 방식으로 요청한 /task/create URL을 처리한다.
     * @param subject       Task 제목
     * @param description   Task 내용
     * @return Task 목록
     */
    @PostMapping("/create")
    public String createTask(@RequestParam String subject, @RequestParam String description, @RequestParam Long estimatedAt) {
        this.taskService.createTask(subject, description, estimatedAt);
        return "redirect:/task/list";
    }

    /**
     * 클라이언트로부터 받은 Task ID를 사용하여 TaskService의 시작 작업을 호출한다.
     * @param taskId Task ID
     * @return task 목록
     */
    @RequestMapping("/start/{taskId}")
    public String startTask(@PathVariable("taskId") Long taskId) {
        taskService.startTask(taskId);
        return "redirect:/task/list";
    }
}
