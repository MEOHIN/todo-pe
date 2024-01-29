package com.meohin.todo_pe.controller;

import com.meohin.todo_pe.TaskStatus;
import com.meohin.todo_pe.entity.SiteUser;
import com.meohin.todo_pe.entity.Task;
import com.meohin.todo_pe.entity.TaskMeasures;
import com.meohin.todo_pe.entity.TaskWithMeasures;
import com.meohin.todo_pe.service.TaskMeasuresService;
import com.meohin.todo_pe.service.TaskService;
import com.meohin.todo_pe.service.UserService;
import com.meohin.todo_pe.validationObject.EstimatedTimeVO;
import com.meohin.todo_pe.validationObject.TaskVO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.nio.charset.StandardCharsets;
import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Task 컨틀롤러 클래스다.
 */
// TaskController 객체가 생성될 때 TaskRepository가 주입되도록 한다.
@RequiredArgsConstructor
@Controller
@PreAuthorize("isAuthenticated()")
// 프리픽스를 지정한다.
@RequestMapping("/task")
public class TaskController {

    // TaskRepository를 사용하여 Task 목록을 조회할 수 있도록 한다.
    private final TaskService taskService;
    private final TaskMeasuresService taskMeasuresService;
    private final UserService userService;

    /**
     * 모든 task를 조회하고, 조회된 목록을 Model 객체에 추가한다.
     * @param model Model 객체
     * @return Task 목록
     */
    @RequestMapping("/list")
    public String list(Model model, Principal principal) {
        SiteUser user = this.userService.getUser(principal.getName());

        List<TaskWithMeasures> taskList = taskService.getTasksWithTaskMeasuresByUserId(user);
        model.addAttribute("tasksWithMeasures", taskList);
        return "task_list";
    }

    /**
     * Task 상세 정보를 조회한다.
     * @param taskId Task ID
     * @param model  Model 객체
     * @return Task 상세
     */
    @RequestMapping(value = "/detail/{taskId}")
    public String detail(Model model, @PathVariable("taskId") Long taskId, Principal principal) {
        // (이전에 세션에 저장된 객체가 있는 경우: 리다이렉트로 상세페이지에 온 경우)
        // 세션에 저장된 Object 타입의 task 객체를 가져와서
        // Task 타입으로 변환하여 Task 타입의 변수인 task에 할당
        Task task = (Task) model.getAttribute("editedTask");

        // (이전에 세션에 저장된 객체가 없는 경우: 다이렉트로 상세페이지에 온 경우)
        if(task == null) {
            // taskId에 해당하는 Task를 가져와서 task에 할당
            task = taskService.getTaskById(taskId);
            if (task == null) return "/error";
        }

        // 다른 유저의 Task면 오류
        SiteUser user = this.userService.getUser(principal.getName());
        if (!taskService.validateUser(user, task)) return "/error";

        // TaskMeasures 리스트를 조회
        List<TaskMeasures> taskMeasureList = taskMeasuresService.getTaskMeasuresByCompleteTimeIsNotNull(taskId);
        ArrayList<Integer> estimatedTimeList = new ArrayList<>();
        ArrayList<Integer> totalTimeList = new ArrayList<>();
        ArrayList<LocalDate> completeTimeList = new ArrayList<>();

        // 완료일 포매팅
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy년 MM월 dd일");

        // 차트에 출력에 필요한 '예상 처리 시간', '실제 처리 시간', '완료일' 리스트를 생성
        for (TaskMeasures taskMeasure : taskMeasureList) {
            estimatedTimeList.add(taskMeasure.getEstimatedAt());

            Integer totalTime = taskMeasure.getTotalElapsedTime();
            totalTimeList.add(totalTime/60);

            LocalDateTime date = taskMeasure.getCompleteTime();
            String completeDate = date.format(formatter);
            completeTimeList.add(LocalDate.parse(completeDate, formatter));
        }

        model.addAttribute("task", task);
        model.addAttribute("taskMeasureList", taskMeasureList);
        model.addAttribute("estimatedTimeList", estimatedTimeList);
        model.addAttribute("totalTimeList", totalTimeList);
        model.addAttribute("completeTimeList", completeTimeList);
        return "task_detail";
    }

    /**
     * tast_form 템플릿을 렌더링하여 출력한다.
     * @param taskVO   Task 등록 입력항목에 대응하는 폼 클래스
     * @return Task 입력 폼
     */
    @GetMapping("/create")
    public String createTask(TaskVO taskVO) {
        return "task_form";
    }

    /**
     * POST 방식으로 요청한 /task/create URL을 처리한다.
     * @param taskVO        입력받은 Task 등록 정보의 유효성을 검증하는 객체
     * @param bindingResult 유효성 검사 결과
     * @param principal     현재 로그인한 사용자
     * @return Task 목록
     */
    @PostMapping("/create")
    public String createTask(@Valid TaskVO taskVO,
                             BindingResult bindingResult,
                             Principal principal) {

        if (bindingResult.hasErrors()) {
            return "task_form";
        }

        // 제목과 내용의 입력값 길이를 바이트 기준으로 검사
        int byteLength;
        boolean isWithinByteLimit;

        // 제목 길이 검증
        String userSubject = taskVO.getInputSubject();  // 사용자 제목 입력값
        byteLength = userSubject.getBytes(StandardCharsets.UTF_8).length;
        isWithinByteLimit = byteLength <= 60;
        if (!isWithinByteLimit) { return "task_form";}

        // 내용 길이 검증
        String userDescription = taskVO.getInputDescription();  // 사용자 내용 입력값
        byteLength = userDescription.getBytes(StandardCharsets.UTF_8).length;
        isWithinByteLimit = byteLength <= 600;
        if (!isWithinByteLimit) { return "task_form"; }

        String userEstimatedTime = taskVO.getInputEstimatedAt();    // 사용자 예상처리시간 입력값

        // 시간 포맷 검사 1
        boolean isValidLength = userEstimatedTime.length() >= 5;    // 길이 검증
        isValidLength = isValidLength && userEstimatedTime.contains(":");   // 필수 문자열 검증
        if (!isValidLength) return "/error";

        // 시간 포맷 검사2
        String[] timeParts = userEstimatedTime.split(":");
        isValidLength = (timeParts[0].length() >= 2 && timeParts[1].length() == 2);
        if (!isValidLength) return "/error";

        // 분 단위로 치환
        int estimatedTime = (Integer.parseInt(timeParts[0]) * 60) + Integer.parseInt(timeParts[1]);

        SiteUser user = this.userService.getUser(principal.getName());

        // 태스크 추가 서비스 호출
        this.taskService.createTask(userSubject.trim(), userDescription, estimatedTime, user);

        return "redirect:/task/list";
    }

    /**
     * Task 제목을 수정하는 페이지로 이동한다.
     * @param model     모델 객체
     * @param taskId    Task ID
     * @param principal 현재 로그인한 사용자
     * @param taskVO    입력받은 Task 등록 정보의 유효성을 검증하는 객체
     * @return  Task 제목 수정 템플릿
     */
    @GetMapping("/modify/{taskId}")
    public String modifyTask(Model model,
                             @PathVariable("taskId") Long taskId,
                             Principal principal,
                             TaskVO taskVO) {

        // Task 정보 불러오기
        Task task = this.taskService.getTaskById(taskId);
        if (task == null) return "/error";

        // 다른 유저의 Task면 오류
        SiteUser user = this.userService.getUser(principal.getName());
        if (!taskService.validateUser(user, task)) return "/error";

        // 정상 Task 정보인 경우 렌더링
        model.addAttribute("task", task);
        return "task_form";
    }

    /**
     * Task 제목과 예상 처리 시간을 수정한다.
     * @param model                 모델 객체
     * @param principal             현재 로그인한 사용자
     * @param taskId                Task ID
     * @param taskVO                입력받은 수정 정보의 유효성을 검증하는 객체
     * @param bindingResult         검증 수행 결과
     * @param redirectAttributes    리다이렉션 후에 데이터를 전달하는 데 사용되는 객체
     * @return  Task 목록
     */
    @PostMapping("modify/{taskId}")
    public String modifyTask(Model model,
                             Principal principal,
                             @PathVariable("taskId") Long taskId,
                             @Valid TaskVO taskVO,
                             BindingResult bindingResult,
                             RedirectAttributes redirectAttributes) {

        // Task 정보를 불러온다.
        Task task = this.taskService.getTaskById(taskId);

        // Model 바인딩이 비정상이면 돌아가기
        if (bindingResult.hasErrors()) {
            model.addAttribute("task", task);
            return "task_form";
        }

        // 태스크 정보가 없으면 오류
        if (task == null) return "/error";

        // 다른 유저의 Task면 오류
        SiteUser user = this.userService.getUser(principal.getName());
        if (!taskService.validateUser(user, task)) return "/error";

        // 제목 초기화
        String title;
        if (taskVO.getInputSubject().length() != 0) {
            title = taskVO.getInputSubject();
        } else {
            title = task.getSubject();
        }

        // 설명 초기화
        String contents;
        if (taskVO.getInputDescription().length() != 0) {
            contents = taskVO.getInputDescription();
        } else {
            contents = task.getDescription();
        }

        // 제목과 내용의 입력값 길이를 바이트 기준으로 검사
        int byteLength;
        boolean isWithinByteLimit;

        // 제목 길이 검증
        byteLength = title.getBytes(StandardCharsets.UTF_8).length;
        isWithinByteLimit = byteLength <= 60;
        if (!isWithinByteLimit) { return "task_form";}

        // 내용 길이 검증
        byteLength = contents.getBytes(StandardCharsets.UTF_8).length;
        isWithinByteLimit = byteLength <= 600;
        if (!isWithinByteLimit) { return "task_form"; }

        // 예상 처리 시간 설정
        int taskEstimatedTime = task.getEstimatedAt();  // 태스크 기본값
        String userEstimatedTime = taskVO.getInputEstimatedAt();    // 사용자 입력값

        // 사용자 입력값이 있으면,
        if (userEstimatedTime.length() != 0) {
            // 시간 포맷 검사 1
            boolean isValidLength = userEstimatedTime.length() >= 5;    // 길이 검즌
            isValidLength = isValidLength && userEstimatedTime.contains(":");   // 필수 문자열 검증
            if (!isValidLength) return "/error";

            // 시간 포맷 검사2
            String[] timeParts = userEstimatedTime.split(":");
            isValidLength = (timeParts[0].length() >= 2 && timeParts[1].length() == 2);
            if (!isValidLength) return "/error";

            // 분 단위로 치환
            taskEstimatedTime = (Integer.parseInt(timeParts[0]) * 60) + Integer.parseInt(timeParts[1]);
        }

        // 신규 실행 태스크 등록 및 태스크 진행상태 변경.
        this.taskService.modifyTask(task, title, contents, taskEstimatedTime);

        // 세션에 태스크 정보 저장
        redirectAttributes.addFlashAttribute(taskId);
        redirectAttributes.addFlashAttribute("editedTask", task);

        // 상세 페이지로 이동
        return String.format("redirect:/task/detail/%s", taskId);
    }

    /**
     * Task 이력을 수정하는 페이지로 이동한다.
     * @param model             모델 객체
     * @param taskMeasuresId    TaskMeasures ID
     * @return  TaskMeasures 수정 폼 템플릿
     */
    @GetMapping("/measures/modify/{taskMeasuresId}")
    public String modifyTaskMeasures(Model model, @PathVariable("taskMeasuresId") Long taskMeasuresId, Principal principal) {
        TaskMeasures taskMeasures = this.taskMeasuresService.getTaskMeasuresById(taskMeasuresId);
        if (taskMeasures == null) return "/error";

        // 다른 유저의 Task면 오류
        SiteUser user = this.userService.getUser(principal.getName());
        if (!taskService.validateUser(user, taskMeasures.getTask())) return "/error";

        String startDate = taskMeasures.getStartTime().format(DateTimeFormatter.ISO_DATE);
        String startTime = 0+taskMeasures.getStartTime().format(DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT).withLocale(Locale.US));
        String completeDate = taskMeasures.getCompleteTime().format(DateTimeFormatter.ISO_DATE);
        String completeTime = 0+taskMeasures.getCompleteTime().format(DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT).withLocale(Locale.US));

        model.addAttribute(taskMeasures);
        model.addAttribute("startDate", startDate);
        model.addAttribute("startTime", startTime);
        model.addAttribute("completeDate", completeDate);
        model.addAttribute("completeTime", completeTime);
        return "measures_modify_form";
    }

    /**
     * Task 이력을 수정한다.
     * @param taskMeasuresId    TaskMeasures ID
     * @param inputCompleteDate      수정할 완료 날짜
     * @param inputCompleteTime      수정할 완료 시각
     * @return  Task 목록 페이지
     */
    @PostMapping("/measures/modify/{taskMeasuresId}")
    public String modifyTaskMeasures(Model model,
                                     @PathVariable("taskMeasuresId") Long taskMeasuresId,
                                     RedirectAttributes redirectAttributes,
                                     @RequestParam String inputCompleteDate,
                                     @RequestParam String inputCompleteTime,
                                     Principal principal) {
        // TaskMeasures 서비스를 사용해서 TaskMeasures ID에 해당하는 TaskMeasures 객체를 검색
        TaskMeasures taskMeasures = this.taskMeasuresService.getTaskMeasuresById(taskMeasuresId);
        if (taskMeasures == null) return "/error";

        Task task = taskMeasures.getTask();
        SiteUser user = this.userService.getUser(principal.getName());
        if (!taskService.validateUser(user, task)) return "/error";

        TaskStatus status = task.getStatus();
        LocalDateTime existingStart = taskMeasures.getStartTime();
        LocalDateTime existingComplete = taskMeasures.getCompleteTime();
        LocalDateTime existingContinue = taskMeasures.getContinueTime();

        // 시간 초기화
        String completeDate = inputCompleteDate;
        String completeTime = inputCompleteTime;

        // 포맷 패턴 정의
        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm a").withLocale(Locale.US);

        if (completeDate.length() == 0) {
            completeDate = existingComplete.format(DateTimeFormatter.ISO_DATE);
        }
        if (completeTime.length() == 0) {
            completeTime = 0+existingComplete.format(DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT).withLocale(Locale.US));
        }

        // 완료 시각 파싱
        if (inputCompleteDate.length() !=0 || inputCompleteTime.length() != 0)  {
            if (status == TaskStatus.STANDBY && existingComplete != null) {
                String resultComplete = completeDate + " " + completeTime;
                LocalDateTime completeDateTime = LocalDateTime.parse(resultComplete, inputFormatter);
                if (((existingContinue != null && completeDateTime.compareTo(existingContinue) > 0)
                        ||
                        completeDateTime.compareTo(existingStart) > 0) && completeDateTime.compareTo(LocalDateTime.now()) < 0) {
                    existingComplete = completeDateTime;
                } else {
                    // 메세지 출력 "수정하려는 완료 시각이 시작 시각과 재시작 시각보다 이후여야 합니다."
                    return "/error";
                }
            } else {
                // 메1세지 출력 "Task를 완료해야만 완료 시각을 수정할 수 있습니다."
                return "/error";
            }
        }

        this.taskMeasuresService.modifyTime(taskMeasures, existingStart, existingComplete);

        // Task 상태에 따라 Task 처리 시간을 계산
        this.taskMeasuresService.calculateTime(taskMeasures, status);

        redirectAttributes.addFlashAttribute("editedTask", task);
        return "redirect:/task/detail/" + task.getId();
    }

    /**
     * Task 이력을 삭제한다.
     * @param model             모델 객체
     * @param taskMeasuresId    TaskMeasures ID
     * @param principal         현재 로그인 사용자 객체
     * @return  Task 목록 페이지로 리다이렉트
     */
    @GetMapping(("/measures/delete/{taskMeasuresId}"))
    public String deleteTaskMeasures(Model model,
                                     @PathVariable("taskMeasuresId") Long taskMeasuresId,
                                     Principal principal,
                                     RedirectAttributes redirectAttributes) {
        TaskMeasures taskMeasures = this.taskMeasuresService.getTaskMeasuresById(taskMeasuresId);
        if (taskMeasures == null) return "/error";

        // 다른 유저의 Task면 오류
        SiteUser user = this.userService.getUser(principal.getName());
        if (!taskService.validateUser(user, taskMeasures.getTask())) return "/error";

        // Task 이력을 삭제
        this.taskMeasuresService.deleteTaskMeasures(taskMeasures);

        Task task = taskMeasures.getTask();
        Long taskId = task.getId();

        // 세션에 태스크 정보 저장
        redirectAttributes.addFlashAttribute(taskId);
        redirectAttributes.addFlashAttribute("editedTask", task);

        // 상세 페이지로 이동
        return String.format("redirect:/task/detail/%s", taskId);
    }

    /**
     * 검색 키워드가 포함된 Task를 검색한다.
     * @param model     모델 객체
     * @param keyword   입력받은 검색 키워드
     * @return  task 목록 템플릿
     */
    @GetMapping("/search")
    public String searchTask(Model model,
                             @RequestParam String keyword,
                             Principal principal) {
        SiteUser user = this.userService.getUser(principal.getName());
        List<TaskWithMeasures> tasks = taskService.getTasksWithTaskMeasuresByUserId(user);
        String keywordWithoutBlank = keyword.trim();
        List<TaskWithMeasures> keywordTasks = taskService.getTasksWithMeasuresBySubjectAndUserId(keywordWithoutBlank, user);
        if (keywordWithoutBlank.length() == 0) {
            model.addAttribute("tasksWithMeasures", tasks);
        } else if (tasks.size() == 0) {
            model.addAttribute("searchResultMessage", "등록된 할 일이 없습니다.");
        } else if (keywordTasks.size() == 0) {
            model.addAttribute("searchResultMessage", "검색결과가 없습니다.");
        } else {
            model.addAttribute("tasksWithMeasures", keywordTasks);
        }
        return "task_list";
    }

    /**
     * Task 시작 및 예상 처리 시간을 수정하는 페이지로 이동한다.
     * @param model             모델 객체
     * @param taskId            Task ID
     * @param principal         현재 로그인한 사용자 객체
     * @param estimatedTimeVO   입력받은 예상 처리 시간의 유효성을 검증하는 객체
     * @return  Task 시작 템플릿
     */
    @GetMapping("/start/{taskId}")
    public String startTask(Model model,
                            @PathVariable("taskId") Long taskId,
                            Principal principal,
                            EstimatedTimeVO estimatedTimeVO) {

        // Task 정보 불러오기
        Task task = this.taskService.getTaskById(taskId);
        if (task == null) return "/error";

        // 다른 유저의 Task면 오류
        SiteUser user = this.userService.getUser(principal.getName());
        if (!taskService.validateUser(user, task)) return "/error";

        // 정상 Task 정보인 경우 렌더링
        model.addAttribute("task", task);
        return "start_form";
    }

    /**
     * POST 방식으로 요청한 시작버튼의 /task/start/{task id} URL을 처리한다.
     * @param model             모델 객체
     * @param principal         현재 로그인한 사용자 객체
     * @param taskId            Task id
     * @param estimatedTimeVO   입력받은 예상 처리 시간의 유효성을 검증하는 객체
     * @param bindingResult     유효성 검사 결과를 저장하는 객체
     * @return  Task 목록 페이지 리다이렉트
     */
    @PostMapping("/start/{taskId}")
    public String startTask(Model model,
                            Principal principal,
                            @PathVariable("taskId") Long taskId,
                            @Valid EstimatedTimeVO estimatedTimeVO,
                            BindingResult bindingResult) {

        // Task 정보를 불러온다.
        Task task = this.taskService.getTaskById(taskId);

        // Model 바인딩이 비정상이면 돌아가기
        if (bindingResult.hasErrors()) {
            model.addAttribute("task", task);
            return "start_form";
        }

        // 태스크 정보가 없으면 오류
        if (task == null) return "/error";

        // 다른 유저의 Task면 오류
        SiteUser user = this.userService.getUser(principal.getName());
        if (!taskService.validateUser(user, task)) return "/error";

        // 예상 처리시간 설정
        int taskEstimatedTime = task.getEstimatedAt(); // 태스크 기본 값
        String userEstimatedTime = estimatedTimeVO.getInputEstimatedAt(); // 사용자 입력 값

        // 사용자 입력값이 있으면,
        if (userEstimatedTime.length() != 0) {
            // 시간 포맷 검사 1
            boolean isValidLength = userEstimatedTime.length() >= 5; // 길이 검증
            isValidLength = isValidLength && userEstimatedTime.contains(":"); // 필수 문자열 검증
            if (!isValidLength) return "/error";

            // 시간 포맷 검사2
            String[] timeParts = userEstimatedTime.split(":");
            isValidLength = (timeParts[0].length() >= 2 && timeParts[1].length() == 2);
            if (!isValidLength) return "/error";

            // 분 단위로 치환
            taskEstimatedTime = (Integer.parseInt(timeParts[0]) * 60) + Integer.parseInt(timeParts[1]);
        }


        // 신규 실행 태스크 등록 및 태스트 진행상태 변경.
        this.taskService.convertTaskStatus(task, TaskStatus.ING);
        this.taskMeasuresService.addTaskMeasures(task, taskEstimatedTime, user);

        // 목록 페이지로 이동
        return "redirect:/task/list";
    }

    @PostMapping("/pause/{taskId}")
    public String pauseTask(Model model, @PathVariable("taskId") Long taskId, Principal principal) {
        Task task = this.taskService.getTaskById(taskId);
        if (task == null) return "/error";

        // 다른 유저의 Task면 오류
        SiteUser user = this.userService.getUser(principal.getName());
        if (!taskService.validateUser(user, task)) return "/error";

        TaskMeasures taskMeasures = this.taskMeasuresService.getTaskMeasuresByCompleteTimeNull(taskId);
        // 일시정지 버튼을 누르면 PAUSE 상태가 돼야 한다.
        this.taskService.convertTaskStatus(task, TaskStatus.PAUSE);
        this.taskMeasuresService.saveTime(taskMeasures, TaskStatus.PAUSE);
        this.taskMeasuresService.calculateTime(taskMeasures, TaskStatus.PAUSE);
        return "redirect:/task/list";
    }

    @PostMapping("/continue/{taskId}")
    public String continueTask(Model model, @PathVariable("taskId") Long taskId, Principal principal) {
        Task task = this.taskService.getTaskById(taskId);
        if (task == null) return "/error";

        // 다른 유저의 Task면 오류
        SiteUser user = this.userService.getUser(principal.getName());
        if (!taskService.validateUser(user, task)) return "/error";

        TaskMeasures taskMeasures = this.taskMeasuresService.getTaskMeasuresByCompleteTimeNull(taskId);
        // 계속 버튼을 누르면 ING 상태가 돼야 한다.
        this.taskService.convertTaskStatus(task, TaskStatus.ING);
        this.taskMeasuresService.saveTime(taskMeasures, TaskStatus.ING);
        return "redirect:/task/list";
    }

    @PostMapping("/complete/{taskId}")
    public String completeTask(Model model, @PathVariable("taskId") Long taskId, Principal principal) {
        Task task = this.taskService.getTaskById(taskId);
        if (task == null) return "/error";

        // 다른 유저의 Task면 오류
        SiteUser user = this.userService.getUser(principal.getName());
        if (!taskService.validateUser(user, task)) return "/error";

        TaskMeasures taskMeasures = this.taskMeasuresService.getTaskMeasuresByCompleteTimeNull(taskId);
        // 완료 버튼을 누르면 STANDBY 상태가 돼야 한다.
        this.taskService.convertTaskStatus(task, TaskStatus.STANDBY);
        this.taskMeasuresService.saveTime(taskMeasures, TaskStatus.STANDBY);
        this.taskMeasuresService.calculateTime(taskMeasures, TaskStatus.STANDBY);
        return "redirect:/task/list";
    }
}
