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
     * @param model     Model 객체
     * @return  Task 목록
     */
    @RequestMapping("/list")
    public String list(Model model) {
        List<Task> taskList = taskService.getTaskList();
        model.addAttribute("taskList", taskList);
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
     * @param subject       Task 제목
     * @param description   Task 내용
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

    // "/start/task id" URL Post 매핑
    @PostMapping("/start/{taskId}")
    // 리턴 타입: String; 템플릿
    // 파라미터:
    //      모델 객체(뷰로 데이터 전달),
    //      경로 변수(task id) 매핑,
    //      요청 매개변수 매핑: TaskStatus, taskMeasures
    public String startTask(Model model, @PathVariable("taskId") Long taskId, @RequestParam TaskStatus ING, @RequestParam TaskMeasures taskMeasures) {

        // task 서비스를 사용해서 task id에 해당하는 Task 객체를 검색
        Task task = this.taskService.getTaskById(taskId);

        // task 서비스의 메서드를 호출하고 task 상태를 standby에서 ing로 변환
        this.taskService.convertTaskStatus(task);

        // taskMeasures 서비스의 메서드를 호출하고 taskMeasures 객체를 생성
        //      : taskMeasures 서비스에 task 이력을 추가하는 메서드를 생성
        this.taskMeasuresService.addTaskMeasures(task, taskMeasures);

        // 반환: task 목록으로 리다이렉트
        return "redirect:/task/list";
    }

    // "/pause/task id" URL Post 매핑
    @PostMapping("/pause/{taskId}")
    // 리턴 타입: String; 템플릿
    // 파라미터:
    //      모델 객체(뷰로 데이터 전달),
    //      경로 변수(task id) 매핑
    public String pauseTask(Model model, @PathVariable("taskId") Long taskId) {
        // task 서비스를 사용해서 task id에 해당하는 Task 객체를 검색
        Task task = this.taskService.getTaskById(taskId);

        // task 서비스의 메서드를 호출하고 task 상태를 ing에서 pause로 변환
        this.taskService.convertTaskStatus();

        // 측정 시간 변수를 생성하고 taskMeasures의 elapsedTime을 할당
        //      :taskMeasures 서비스의 메서드를 호출하고 taskMeasures 객체를 생성
        //          : taskMeasures의 pauseTime을 저장
        //          : taskMeasures의 elapsedPausedTime을 저장
        this.taskMeasuresService.saveTime();

        // 반환: task 목록으로 리다이렉트
        return "redirect:/task/list";
    }

    // "/continue/task id" URL Post 매핑
    @PostMapping("/continue/{taskId}")
    // 리턴 타입: String; 템플릿
    // 파라미터:
    //      모델 객체(뷰로 데이터 전달),
    //      경로 변수(task id) 매핑,
    public String continueTask(Model model, @PathVariable("taskId") Long taskId) {
        // task 서비스를 사용해서 task id에 해당하는 Task 객체를 검색
        Task task = this.taskService.getTaskById(taskId);

        // task 서비스의 메서드를 호출하고 task 상태를 pause에서 ing로 변환
        this.taskService.convertTaskStatus();

        // taskMeasures 서비스의 메서드를 호출하고 taskMeasures 객체를 생성
        //          : taskMeasures의 continueTime을 저장
        this.taskMeasuresService.saveTime();

        // 반환: task 목록으로 리다이렉트
        return "redirect:/task/list";
    }

    // "/complete/task id" URL Post 매핑
    @PostMapping("/complete/{taskId}")
    // 리턴 타입: String; 템플릿
    // 파라미터:
    //      모델 객체(뷰로 데이터 전달),
    //      경로 변수(task id) 매핑,
    public String completeTask(Model model, @PathVariable("taskId") Long taskId) {
        // task 서비스를 사용해서 task id에 해당하는 Task 객체를 검색
        Task task = this.taskService.getTaskById(taskId);

        // task 서비스의 메서드를 호출하고 task 상태를 ING에서 STANDBY로 변환
        this.taskService.convertTaskStatus();

        // taskMeasures 서비스의 메서드를 호출하고 taskMeasures 객체를 생성
        //          : taskMeasures의 completeTime을 저장
        //          : totalElapsedTime 저장
        //            : elapsedPausedTime가 있으면 elapsedPausedTime과 elapsedCompletedTime의 합을 저장
        //            : elapsedPausedTime가 없으면 totalElapsedTime에 startTime부터 completeTime까지 걸린 시간을 저장
        this.taskMeasuresService.saveTime();

        // 반환: task 목록으로 리다이렉트
        return "redirect:/task/list";
    }
}
