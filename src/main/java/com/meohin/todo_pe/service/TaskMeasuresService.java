package com.meohin.todo_pe.service;

import com.meohin.todo_pe.entity.TaskMeasures;
import com.meohin.todo_pe.repository.TaskMeasuresRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class TaskMeasuresService {

    private final TaskMeasuresRepository taskMeasuresRepository;

    public void addTaskMeasures(TaskMeasures taskMeasures) {
        taskMeasuresRepository.save(taskMeasures);
    }
}
