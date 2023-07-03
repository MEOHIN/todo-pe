package com.meohin.todo_pe.controller;

import com.meohin.todo_pe.TaskStatus;
import com.meohin.todo_pe.entity.Task;
import com.meohin.todo_pe.entity.TaskMeasures;
import com.meohin.todo_pe.service.TaskMeasuresService;
import com.meohin.todo_pe.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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

    // TaskRepository를 사용하여 Task 목록을 조회할 수 있도록 한다.
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
     * Task 상세 정보를 조회한다.
     * @param taskId Task ID
     * @param model  Model 객체
     * @return Task 상세
     */
    @RequestMapping(value = "/detail/{taskId}")
    public String detail(@PathVariable("taskId") Long taskId, Model model) {
        // (이전에 세션에 저장된 객체가 있는 경우: 리다이렉트로 상세페이지에 온 경우)
        // 세션에 저장된 Object 타입의 task 객체를 가져와서
        // Task 타입으로 변환하여 Task 타입의 변수인 task에 할당
        Task task = (Task) model.getAttribute("editedTask");

        // (이전에 세션에 저장된 객체가 없는 경우: 다이렉트로 상세페이지에 온 경우)
        if(task == null) {
            // taskId에 해당하는 Task를 가져와서 task에 할당
            task = taskService.getTaskById(taskId);
        }

        // TaskMeasures 리스트를 조회한다.
        List<TaskMeasures> taskMeasureList = taskMeasuresService.getTaskMeasureList(taskId);
        model.addAttribute("task", task);
        // 모델 객체에 taskMeasuresList를 추가한다.
        model.addAttribute("taskMeasureList", taskMeasureList);
        return "task_detail";
    }

    /**
     * tast_form 템플릿을 렌더링하여 출력한다.
     * @return Task 입력 폼
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
     * Task 제목을 수정하는 페이지로 이동한다.
     * @param model     모델 객체
     * @param taskId    Task ID
     * @return  Task 제목 수정 템플릿
     */
    @GetMapping("/modify/{taskId}")
    public String modifyTask(Model model, @PathVariable("taskId") Long taskId) {
        Task task = this.taskService.getTaskById(taskId);
        model.addAttribute("task", task);
        return "task_modify_form";
    }

    /**
     * Task 제목을 수정한다.
     * @param model     모델 객체
     * @param taskId    Task ID
     * @param subject   입력받은 수정된 Task 제목
     * @return  Task 목록
     */
    @PostMapping("modify/{taskId}")
    public String modifyTask(Model model, RedirectAttributes redirectAttributes, @PathVariable("taskId") Long taskId, @RequestParam String subject) {
        Task task = this.taskService.getTaskById(taskId);
        this.taskService.modifySubject(task, subject);
        redirectAttributes.addFlashAttribute(taskId);
        redirectAttributes.addFlashAttribute("editedTask", task);
        return "redirect:/task/detail/{taskId}";
    }

    /**
     * Task 이력을 수정하는 페이지로 이동한다.
     * @param model             모델 객체
     * @param taskMeasuresId    TaskMeasures ID
     * @return  TaskMeasures 수정 폼 템플릿
     */
    @GetMapping("/measures/modify/{taskMeasuresId}")
    public String modifyTaskMeasures(Model model, @PathVariable("taskMeasuresId") Long taskMeasuresId) {
        TaskMeasures taskMeasures = this.taskMeasuresService.getTaskMeasuresById(taskMeasuresId);
        model.addAttribute(taskMeasures);
        return "measures_modify_form";
    }

    /**
     * POST 방식으로 요청한 시작버튼의 /task/start/{task id} URL을 처리한다.
     * @param taskId Task id
     * @return Task 목록 페이지 리다이렉트
     */
    @PostMapping("/start/{taskId}")
    public String startTask(@PathVariable("taskId") Long taskId) {

        Task task = this.taskService.getTaskById(taskId);
        // 시작 버튼을 누르면 ING 상태가 돼야 한다.
        this.taskService.convertTaskStatus(task, TaskStatus.ING);
        this.taskMeasuresService.addTaskMeasures(task);
        return "redirect:/task/list";
    }

    @PostMapping("/pause/{taskId}")
    public String pauseTask(@PathVariable("taskId") Long taskId) {
        Task task = this.taskService.getTaskById(taskId);
        TaskMeasures taskMeasures = this.taskMeasuresService.getTaskMeasuresByCompleteTimeNull(taskId);
        // 일시정지 버튼을 누르면 PAUSE 상태가 돼야 한다.
        this.taskService.convertTaskStatus(task, TaskStatus.PAUSE);
        this.taskMeasuresService.saveTime(taskMeasures, TaskStatus.PAUSE);
        this.taskMeasuresService.calculateTime(taskMeasures, TaskStatus.PAUSE);
        return "redirect:/task/list";
    }

    @PostMapping("/continue/{taskId}")
    public String continueTask( @PathVariable("taskId") Long taskId) {
        Task task = this.taskService.getTaskById(taskId);
        TaskMeasures taskMeasures = this.taskMeasuresService.getTaskMeasuresByCompleteTimeNull(taskId);
        // 계속 버튼을 누르면 ING 상태가 돼야 한다.
        this.taskService.convertTaskStatus(task, TaskStatus.ING);
        this.taskMeasuresService.saveTime(taskMeasures, TaskStatus.ING);
        return "redirect:/task/list";
    }

    @PostMapping("/complete/{taskId}")
    public String completeTask(@PathVariable("taskId") Long taskId) {
        Task task = this.taskService.getTaskById(taskId);
        TaskMeasures taskMeasures = this.taskMeasuresService.getTaskMeasuresByCompleteTimeNull(taskId);
        // 완료 버튼을 누르면 STANDBY 상태가 돼야 한다.
        this.taskService.convertTaskStatus(task, TaskStatus.STANDBY);
        this.taskMeasuresService.saveTime(taskMeasures, TaskStatus.STANDBY);
        this.taskMeasuresService.calculateTime(taskMeasures, TaskStatus.STANDBY);
        return "redirect:/task/list";
    }
}
