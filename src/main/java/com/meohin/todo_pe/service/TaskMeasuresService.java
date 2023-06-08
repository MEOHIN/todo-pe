package com.meohin.todo_pe.service;

import com.meohin.todo_pe.entity.Task;
import com.meohin.todo_pe.entity.TaskMeasures;
import com.meohin.todo_pe.repository.TaskMeasuresRepository;
import com.meohin.todo_pe.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Service
public class TaskMeasuresService {

    private final TaskRepository taskRepository;
    private final TaskMeasuresRepository taskMeasuresRepository;


    /**
     * Task ID 받아와서 해당 Task를 조회하고 TaskMeasures 객체를 생성하고 task_measures 테이블에 추가한다.
     * @param taskId Task ID
     */
    public void startTask(Long taskId) {
        Task task = taskRepository.findById(taskId).orElseThrow(() -> new RuntimeException("Task not found"));

        // 신규 태스크 이력 생성
        TaskMeasures taskMeasures = new TaskMeasures();
        taskMeasures.setTask(task);
        taskMeasures.setStartTime(LocalDateTime.now());
        taskMeasures.setElapsedTime(null);
        taskMeasures.setCompleteTime(null);

        // 추가 후 결과 전달
        taskMeasuresRepository.save(taskMeasures);
    }

    public boolean addTaskMeasures(TaskMeasures taskMeasures) {
        try {
            taskMeasuresRepository.save(taskMeasures);
            return true;
        } catch(Exception e1) {
            System.out.println("TaskMeasuresService:L18");
            return false;
        }
    }
}
