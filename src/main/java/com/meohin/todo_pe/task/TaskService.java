package com.meohin.todo_pe.task;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class TaskService {

    private final TaskRepository taskRepository;

    public List<TaskDTO> getTaskList() {
        List<Task> taskList = taskRepository.findAll(); // 모든 작업(Task)을 조회
        return taskList.stream().map(this::convertToDTO).collect(Collectors.toList());  // 변환된 TaskDTO 객체들은 리스트로 수집되어 반환
    }

    private TaskDTO convertToDTO(Task task) {   // Task 엔티티를 TaskDTO로 변환
        TaskDTO taskDTO = new TaskDTO();
        // Task 엔티티의 필드 값을 TaskDTO 객체에 설정하여 변환
        taskDTO.setId(task.getId());
        taskDTO.setSubject(task.getSubject());
        taskDTO.setDescription(task.getDescription());
        taskDTO.setEstimatedAt(task.getEstimatedAt());
        return taskDTO;
    }
}
