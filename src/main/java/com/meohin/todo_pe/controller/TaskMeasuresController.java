package com.meohin.todo_pe.controller;

import com.meohin.todo_pe.dao.TaskDTO;
import com.meohin.todo_pe.service.TaskMeasuresService;
import com.meohin.todo_pe.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RequiredArgsConstructor
@Controller
public class TaskMeasuresController {

    private final TaskService taskService;
    private final TaskMeasuresService taskMeasuresService;

    /**
     * 클라이언트로부터 받은 Task ID를 사용하여 TaskService의 시작 작업을 호출한다.
     * @param elapsedTime   경과 시간
     * @return  Task 목록
     */
    @PostMapping("/start/{taskId}")
    public String startTask(@PathVariable("taskId") Long taskId, @RequestParam Long elapsedTime) {

//        // taskId 정상
//        boolean result = taskMeasuresService.startTask(taskId);
//
//        // 결과 처리
//        if(result) {
//            return "redirect:/task/list";
//        } else {
//            return "redirect:/task/list/fail";
//        }

        TaskDTO taskDTO = taskService.getTaskById(taskId);
        this.taskMeasuresService.startTask(elapsedTime);

        return "redirect:/task/list";
    }
}
