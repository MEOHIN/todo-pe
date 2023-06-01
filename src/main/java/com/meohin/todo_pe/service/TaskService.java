package com.meohin.todo_pe.service;

import com.meohin.todo_pe.dao.TaskDTO;
import com.meohin.todo_pe.entity.Task;
import com.meohin.todo_pe.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class TaskService {

    private final TaskRepository taskRepository;

    /**
     * 모든 Task를 조회하고 변환된 Task DTO 객체들을 리스트로 반환한다.
     * @return Task DTO 리스트
     */
    public List<TaskDTO> getTaskList() {
        // 모든 Task를 조회한다.
        List<Task> taskList = taskRepository.findAll();
        // 변환된 TaskDTO 객체들은 리스트로 수집되어 반환한다.
        return taskList.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    /**
     * Task를 Task DTO로 변환후 반환한다.
     * @param taskId Task ID
     * @return Task DTO
     */
    public TaskDTO getTaskById(Long taskId) {
        // taskRepository를 사용하여 해당 taskID에 해당하는 Task를 조회한다.
        Task task = taskRepository.findById(taskId).orElse(null);
        if (task != null) {
            // Task가 존재하면 convertToDTO()메서드를 사용해서 Task를 TaskDTO로 변환 후 반환한다.
            return convertToDTO(task);
        }
        // Task가 존재하지 않는 경우(taskRepository.findById(taskId)가 null을 반환하는 경우)에는 null을 반환한다.
        return null;
    }

    /**
     * Task 엔티티를 TaskDTO로 변환한다.
     * @param task Task
     * @return Task DTO
     */
    private TaskDTO convertToDTO(Task task) {
        TaskDTO taskDTO = new TaskDTO();
        // Task 엔티티의 필드 값을 TaskDTO 객체에 설정하여 변환한다.
        taskDTO.setId(task.getId());
        taskDTO.setSubject(task.getSubject());
        taskDTO.setDescription(task.getDescription());
        taskDTO.setEstimatedAt(task.getEstimatedAt());
        return taskDTO;
    }
}

