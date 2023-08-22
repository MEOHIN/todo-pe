package com.meohin.todo_pe.service;

import com.meohin.todo_pe.TaskStatus;
import com.meohin.todo_pe.entity.SiteUser;
import com.meohin.todo_pe.entity.Task;
import com.meohin.todo_pe.entity.TaskMeasures;
import com.meohin.todo_pe.repository.TaskMeasuresRepository;
import com.meohin.todo_pe.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Service
public class TaskMeasuresService {

    private final TaskMeasuresRepository taskMeasuresRepository;
    private final TaskRepository taskRepository;

    /**
     * 해당 taskMeasures ID에 해당하는 TaskMeasures 객체를 조회하고 반환한다.
     * @param taskMeasuresId    taskMeasures ID
     * @return  taskMeasures 객체
     */
    public TaskMeasures getTaskMeasuresById(Long taskMeasuresId) {
        return taskMeasuresRepository.findById(taskMeasuresId).orElse(null);
    }

    /**
     * Task ID 및 완료되지 않은 Task 이력을 조회한다.
     * @param taskId  Task
     * @return  조회한 TaskMeasures
     */
    public TaskMeasures getTaskMeasuresByCompleteTimeNull(Long taskId) {
        return taskMeasuresRepository.findByTaskIdAndCompleteTimeNull(taskId);
    }

    /**
     * Task ID 및 완료 시각이 존재하는 Task 이력을 조회한다.
     * @param taskId  Task
     * @return  조회한 TaskMeasures
     */
    public List<TaskMeasures> getTaskMeasuresByCompleteTimeIsNotNull(Long taskId) {
        return taskMeasuresRepository.findByTaskIdAndCompleteTimeNotNull(taskId);
    }

    /**
     * Task ID로 데이터를 조회
     * @param taskId    Task ID
     * @return  TaskMeasures List
     */
    public List<TaskMeasures> getTaskMeasureList(Long taskId) {
        return taskMeasuresRepository.findByTaskId(taskId);
    }

    /**
     * Task 이력을 저장한다.
     * @param task Task
     */
    public void addTaskMeasures(Task task, Integer estimatedAt, SiteUser user) {
        TaskMeasures taskMeasures = new TaskMeasures();

        taskMeasures.setTask(task);
        taskMeasures.setEstimatedAt(estimatedAt);
        taskMeasures.setStartTime(LocalDateTime.now().withNano(0));
        taskMeasures.setSiteUser(user);

        taskMeasuresRepository.save(taskMeasures);
    }

    /**
     * Task의 상태 전환 시각을 저장한다.
     * @param taskMeasures      TaskMeasures
     * @param timeStatusToSet   저장하려는 시각의 Task 상태
     */
    public void saveTime(TaskMeasures taskMeasures, TaskStatus timeStatusToSet) {

        if (timeStatusToSet == TaskStatus.PAUSE) {
            taskMeasures.setPauseTime(LocalDateTime.now().withNano(0));
        }
        else if (timeStatusToSet == TaskStatus.ING) {
            taskMeasures.setContinueTime(LocalDateTime.now().withNano(0));
        }
        else if (timeStatusToSet == TaskStatus.STANDBY) {
            taskMeasures.setCompleteTime(LocalDateTime.now().withNano(0));
        }
        taskMeasuresRepository.save(taskMeasures);
    }

    /**
     * Task의 진행 시간을 계산한다.
     * @param taskMeasures  TaskMeasures
     * @param taskStatus    저장하려는 시점의 task 상태
     */
    public void calculateTime(TaskMeasures taskMeasures, TaskStatus taskStatus) {
        if (taskStatus == TaskStatus.PAUSE) {
            if (taskMeasures.getContinueTime() == null) {
                Duration elapsedPausedDuration = Duration.between(taskMeasures.getStartTime(), taskMeasures.getPauseTime());
                int elapsedPausedTime = (int) elapsedPausedDuration.toMinutes();
                taskMeasures.setElapsedPausedTime(elapsedPausedTime);
            } else {
                Duration elapsedPausedDuration = Duration.between(taskMeasures.getContinueTime(), taskMeasures.getPauseTime());
                int elapsedPausedTimeFromContinue = (int) elapsedPausedDuration.toMinutes();
                taskMeasures.setElapsedPausedTime(taskMeasures.getElapsedPausedTime() + elapsedPausedTimeFromContinue);
            }
        }
        else if (taskStatus == TaskStatus.STANDBY) {
            if (taskMeasures.getContinueTime() == null) {
                Duration totalElapsedDuration = Duration.between(taskMeasures.getStartTime(), taskMeasures.getCompleteTime());
                int totalElapsedTime = (int) totalElapsedDuration.toMinutes();
                taskMeasures.setTotalElapsedTime(totalElapsedTime);
            }
            else {
                Duration elapsedCompletedDuration = Duration.between(taskMeasures.getContinueTime(), taskMeasures.getCompleteTime());
                int elapsedCompletedTime = (int) elapsedCompletedDuration.toMinutes();
                taskMeasures.setElapsedCompletedTime(elapsedCompletedTime);

                taskMeasures.setTotalElapsedTime(taskMeasures.getElapsedPausedTime() + elapsedCompletedTime);
            }
        }
        taskMeasuresRepository.save(taskMeasures);
    }

    /**
     * 예상 처리 시간, 시작 시각, 완료 시각을 수정한다.
     * @param taskMeasures  TaskMeasures 객체
     * @param startTime     수정할 시작 시각
     * @param completeTime  수정할 완료 시각
     */
    public void modifyTime(TaskMeasures taskMeasures, LocalDateTime startTime, LocalDateTime completeTime) {
        // Task 시작 시각 설정
        taskMeasures.setStartTime(startTime);
        // Task 완료 시각 설정
        taskMeasures.setCompleteTime(completeTime);

        // 이력 수정 시각 설정
        taskMeasures.setModifiedAt(LocalDateTime.now().withNano(0));

        // 세팅 저장
        taskMeasuresRepository.save(taskMeasures);
    }

    /**
     * Task 이력을 삭제한다.
     * @param taskMeasures    TaskMeasures
     */
    public void deleteTaskMeasures(TaskMeasures taskMeasures) {
        this.taskMeasuresRepository.delete(taskMeasures);
    }
}
