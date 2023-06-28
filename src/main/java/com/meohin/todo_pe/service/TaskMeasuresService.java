package com.meohin.todo_pe.service;

import com.meohin.todo_pe.TaskStatus;
import com.meohin.todo_pe.entity.Task;
import com.meohin.todo_pe.entity.TaskMeasures;
import com.meohin.todo_pe.repository.TaskMeasuresRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Service
public class TaskMeasuresService {

    private final TaskMeasuresRepository taskMeasuresRepository;

    /**
     * Task ID 및 완료 시간으로 데이터를 조히
     * @param taskId  Task
     * @return  조회한 TaskMeasures
     */
    public TaskMeasures getTaskMeasuresByCompleteTimeNull(Long taskId) {
        return taskMeasuresRepository.findByTaskIdAndCompleteTimeNull(taskId);
    }

    /**
     * Task ID로 데이터를 조회
     * @param taskId
     * @return
     */
    public List<TaskMeasures> getTaskMeasureList(Long taskId) {
        return taskMeasuresRepository.findByTaskId(taskId);
    }

    /**
     * Task 이력을 저장한다.
     * @param task Task
     */
    public void addTaskMeasures(Task task) {
        TaskMeasures taskMeasures = new TaskMeasures();

        taskMeasures.setTask(task);
        taskMeasures.setStartTime(LocalDateTime.now());

        taskMeasuresRepository.save(taskMeasures);
    }

    /**
     * Task의 상태 전환 시각을 저장한다.
     * @param taskMeasures      TaskMeasures
     * @param timeStatusToSet   저장하려는 시각의 Task 상태
     */
    public void saveTime(TaskMeasures taskMeasures, TaskStatus timeStatusToSet) {

        if (timeStatusToSet == TaskStatus.PAUSE) {
            taskMeasures.setPauseTime(LocalDateTime.now());
        }
        else if (timeStatusToSet == TaskStatus.ING) {
            taskMeasures.setContinueTime(LocalDateTime.now());
        }
        else if (timeStatusToSet == TaskStatus.STANDBY) {
            taskMeasures.setCompleteTime(LocalDateTime.now());
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
                int elapsedPausedTime = (int) elapsedPausedDuration.toSeconds();
                taskMeasures.setElapsedPausedTime(elapsedPausedTime);
            } else {
                Duration elapsedPausedDuration = Duration.between(taskMeasures.getContinueTime(), taskMeasures.getPauseTime());
                int elapsedPausedTimeFromContinue = (int) elapsedPausedDuration.toSeconds();
                taskMeasures.setElapsedPausedTime(taskMeasures.getElapsedPausedTime() + elapsedPausedTimeFromContinue);
            }
        }
        else if (taskStatus == TaskStatus.STANDBY) {
            if (taskMeasures.getContinueTime() == null) {
                Duration totalElapsedDuration = Duration.between(taskMeasures.getStartTime(), taskMeasures.getCompleteTime());
                int totalElapsedTime = (int) totalElapsedDuration.toSeconds();
                taskMeasures.setTotalElapsedTime(totalElapsedTime);
            }
            else {
                Duration elapsedCompletedDuration = Duration.between(taskMeasures.getContinueTime(), taskMeasures.getCompleteTime());
                int elapsedCompletedTime = (int) elapsedCompletedDuration.toSeconds();
                taskMeasures.setElapsedCompletedTime(elapsedCompletedTime);

                taskMeasures.setTotalElapsedTime(taskMeasures.getElapsedPausedTime() + elapsedCompletedTime);
            }
        }
        taskMeasuresRepository.save(taskMeasures);
    }
}
