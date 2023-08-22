package com.meohin.todo_pe.service;

import com.meohin.todo_pe.TaskStatus;
import com.meohin.todo_pe.entity.SiteUser;
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

    /**
     * 모든 Task를 조회하고 리스트로 반환한다.
     * @return Task 리스트
     */
    public List<Task> getTaskList(SiteUser user) {
        // 모든 Task를 조회하고 반환한다.
        return taskRepository.findTasksBySiteUser(user);
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

    public List<Task> getTaskByKeyword(String subject, SiteUser user) {
        return taskRepository.findTasksBySubjectContainsAndSiteUser(subject, user);
    }

    /**
     * 입력받은 Task 데이터를 저장한다.
     * @param subject       Task 제목
     * @param description   Task 내용
     * @param estimatedAt   Task 예상 시간
     */
    public void createTask(String subject, String description, Integer estimatedAt, SiteUser user) {
        Task task = new Task();

        task.setCreatedAt(LocalDateTime.now().withNano(0));
        task.setSubject(subject);
        task.setDescription(description);
        task.setEstimatedAt(estimatedAt);
        task.setSiteUser(user);

        taskRepository.save(task);
    }

    /**
     * Task의 제목을 수정한다.
     * @param task      Task
     * @param subject   수정하려는 Task 제목
     */
    public void modifyTask(Task task, String subject, int estimatedAt) {
        task.setSubject(subject);
        task.setEstimatedAt(estimatedAt);
        task.setModifiedAt(LocalDateTime.now().withNano(0));
        taskRepository.save(task);
    }

    /**
     * 입력받은 Status로 작업 상태를 변환한다.
     * @param task      Task
     * @param setStatus 설정하려는 Status
     */
    public void convertTaskStatus(Task task, TaskStatus setStatus) {
        if (setStatus == TaskStatus.ING) {
            task.setStatus(TaskStatus.ING);
        } else if (setStatus == TaskStatus.STANDBY) {
            task.setStatus(TaskStatus.STANDBY);
        } else if (setStatus == TaskStatus.PAUSE) {
            task.setStatus(TaskStatus.PAUSE);
        }
        taskRepository.save(task);
    }

    /**
     * 현재 로그인한 사용자와 해당 사용자가 접근하려는 Task의 사용자 정보가 동일한지 검증한다.
     * @param user  로그인한 사용자
     * @param task  사용자가 접근하려는 Task
     * @return  검증결과: TRUE/FALSE
     */
    public boolean validateUser(SiteUser user, Task task) {

        String currentUserId = user.getUserId();
        String taskUserId = task.getSiteUser().getUserId();

        return currentUserId.equals(taskUserId);
    }
}

