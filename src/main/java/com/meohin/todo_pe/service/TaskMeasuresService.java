package com.meohin.todo_pe.service;

import com.meohin.todo_pe.entity.Task;
import com.meohin.todo_pe.entity.TaskMeasures;
import com.meohin.todo_pe.repository.TaskMeasuresRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Service
public class TaskMeasuresService {

    private final TaskMeasuresRepository taskMeasuresRepository;

    // 리턴 타입: void
    // 파라미터: task 객체
    public void addTaskMeasures(Task task) {
        // TaskMeasures 객체를 생성
        TaskMeasures taskMeasures = new TaskMeasures();

        // task 이력 설정
        taskMeasures.setTask(task);
        taskMeasures.setStartTime(LocalDateTime.now());

        // task 이력 저장
        taskMeasuresRepository.save(taskMeasures);
    }


}
