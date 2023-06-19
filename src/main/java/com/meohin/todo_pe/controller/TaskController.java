package com.meohin.todo_pe.controller;

import com.meohin.todo_pe.TaskStatus;
import com.meohin.todo_pe.entity.Task;
import com.meohin.todo_pe.service.TaskMeasuresService;
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
    private final TaskMeasuresService taskMeasuresService;

    /**
     * 모든 task를 조회하고, 조회된 목록을 Model 객체에 추가한다.
     * @param model Model 객체
     * @return Task 목록
     */
    @RequestMapping("/list")
    public String list(Model model) {
        List<Task> taskList = taskService.getTaskList();
        model.addAttribute("taskList", taskList);
        return "task_list";
    }

    /**
     * 작업 상세 정보를 조회한다.
     * @param taskId Task ID
     * @param model  Model 객체
     * @return Task 상세
     */
    @RequestMapping(value = "/detail/{taskId}")
    public String detail(@PathVariable("taskId") Long taskId, Model model) {
        Task task = taskService.getTaskById(taskId);
        model.addAttribute("task", task);
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
     * @param subject     Task 제목
     * @param description Task 내용
     * @return Task 목록
     */
    @PostMapping("/create")
    public String createTask(@RequestParam String subject, @RequestParam String description, @RequestParam String estimatedAt) {

        // 예상시간 파싱
        // 00:00 포맷으로 정해진 문자열을 파싱해서 분단위로 맞춰준다.
        int hour = Integer.parseInt(estimatedAt.substring(0, 2));
        int minutes = Integer.parseInt(estimatedAt.substring(3, 5));
        for(int i=0; i<hour; i++) {
            minutes += 60;
        }

        // 태스크 추가 서비스 호출
        this.taskService.createTask(subject, description, minutes);

        return "redirect:/task/list";
    }

    /**
     * POST 방식으로 요청한 시작버튼의 /task/start/{task id} URL을 처리한다.
     *
     * @param model  모델 객체
     * @param taskId Task id
     * @return Task 목록 페이지 리다이렉트
     */
    @PostMapping("/start/{taskId}")
    public String startTask(Model model, @PathVariable("taskId") Long taskId) {

        Task task = this.taskService.getTaskById(taskId);
        // 시작 버튼을 누르면 ING 상태가 돼야 한다.
        this.taskService.convertTaskStatus(TaskStatus.ING);
        this.taskMeasuresService.addTaskMeasures(task);
        return "redirect:/task/list";
    }

    @PostMapping("/pause/{taskId}")
    public String pauseTask(Model model, @PathVariable("taskId") Long taskId) {
        Task task = this.taskService.getTaskById(taskId);
        // 일시정지 버튼을 누르면 PAUSE 상태가 돼야 한다.
        this.taskService.convertTaskStatus(TaskStatus.PAUSE);
        this.taskMeasuresService.saveTime();
        return "redirect:/task/list";
    }

    @PostMapping("/continue/{taskId}")
    public String continueTask(Model model, @PathVariable("taskId") Long taskId) {
        Task task = this.taskService.getTaskById(taskId);
        // 계속 버튼을 누르면 ING 상태가 돼야 한다.
        this.taskService.convertTaskStatus(TaskStatus.ING);
        this.taskMeasuresService.saveTime();
        return "redirect:/task/list";
    }

    @PostMapping("/complete/{taskId}")
    public String completeTask(Model model, @PathVariable("taskId") Long taskId) {
        Task task = this.taskService.getTaskById(taskId);
        // 완료 버튼을 누르면 STANDBY 상태가 돼야 한다.
        this.taskService.convertTaskStatus(TaskStatus.STANDBY);
        this.taskMeasuresService.saveTime();
        return "redirect:/task/list";
    }
}
