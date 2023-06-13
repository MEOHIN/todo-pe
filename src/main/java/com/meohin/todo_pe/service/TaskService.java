package com.meohin.todo_pe.service;

import com.meohin.todo_pe.entity.Task;
import com.meohin.todo_pe.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final TaskMeasuresService taskMeasuresService;

    /**
     * 모든 Task를 조회하고 리스트로 반환한다.
     * @return Task 리스트
     */
    public List<Task> getTaskList() {
        // 모든 Task를 조회하고 반환한다.
        return taskRepository.findAll();
    }

    /**
     * Task를 반환한다.
     * @param taskId Task ID
     * @return Task
     */
    public Task getTaskById(Long taskId) {
        // taskRepository를 사용하여 해당 taskID에 해당하는 Task를 조회하고 반환한다.
        return taskRepository.findById(taskId).orElse(null);
    }

    /**
     * 입력받은 Task 데이터를 저장한다.
     * @param subject       Task 제목
     * @param description   Task 내용
     * @param estimatedAt   Task 예상 시간
     */
    public void createTask(String subject, String description, Integer estimatedAt) {
        Task task = new Task();

        task.setCreatedAt(LocalDateTime.now());
        task.setSubject(subject);
        task.setDescription(description);
        task.setEstimatedAt(estimatedAt);

        taskRepository.save(task);
    }
}

