package com.meohin.todo_pe.service;

import com.meohin.todo_pe.TaskStatus;
import com.meohin.todo_pe.entity.Task;
import com.meohin.todo_pe.entity.TaskMeasures;
import com.meohin.todo_pe.repository.TaskMeasuresRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;

@RequiredArgsConstructor
@Service
public class TaskMeasuresService {

    private final TaskMeasuresRepository taskMeasuresRepository;

    /**
     * 작업 ID 및 완료 시간으로 데이터를 조히
     * @param taskId  Task
     * @return  조회한 TaskMeasures
     */
    public TaskMeasures getTaskMeasuresByCompleteTimeNull(Long taskId) {
        return taskMeasuresRepository.findByTaskIdAndCompleteTimeNull(taskId, null);
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

    // saveTime 메서드
    // 리턴 타입: void
    // 파라미터: TaskMeasures, 설정하려는 시각 상태
    public void saveTime(TaskMeasures taskMeasures, TaskStatus timeStatusToSet) {
        // 조건문
        //  설정하려는 시각이 pause 시각이면 pauseTime 설정
        if (timeStatusToSet == TaskStatus.PAUSE) {
            taskMeasures.setPauseTime(LocalDateTime.now());
        }
        //  설정하려는 시각이 continue 시각이면 continueTime 설정
        else if (timeStatusToSet == TaskStatus.ING) {
            taskMeasures.setContinueTime(LocalDateTime.now());
        }
        //  설정하려는 시각이 complete 시각면 completeTime 설정
        else if (timeStatusToSet == TaskStatus.STANDBY) {
            taskMeasures.setCompleteTime(LocalDateTime.now());
        }

        // 설정 저장
        taskMeasuresRepository.save(taskMeasures);
    }

    // 시간을 계산하는 메서드
    // 리턴 타입: void
    // 파라미터: TaskMeasures
    public void calculateTime(TaskMeasures taskMeasures, TaskStatus taskStatus) {
        // 일시정지 버튼일 때
        if (taskStatus == TaskStatus.PAUSE) {
            // elapsedPausedTime 설정
            //  : startTime 과 pauseTime 사이의 시간 간격을 계산
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

        // 완료 버튼일 때
        else if (taskStatus == TaskStatus.STANDBY) {
            // elapsedCompletedTime 설정
            //  : continueTime 과 completeTime 사이의 시간 간격을 계산
            Duration elapsedCompletedDuration = Duration.between(taskMeasures.getContinueTime(), taskMeasures.getCompleteTime());
            int elapsedCompletedTime = (int) elapsedCompletedDuration.toSeconds();
            taskMeasures.setElapsedCompletedTime(elapsedCompletedTime);

            // totalElapsedTime 설정
            // 조건문
            //  continueTime 이 없으면 startTime 과 completeTime 사이의 시간 간격을 계산
            if (taskMeasures.getContinueTime() == null) {
                Duration totalElapsedDuration = Duration.between(taskMeasures.getStartTime(), taskMeasures.getCompleteTime());
                int totalElapsedTime = (int) totalElapsedDuration.toSeconds();
                taskMeasures.setTotalElapsedTime(totalElapsedTime);
            }
            //  continueTime 이 있으면 elapsedPausedTime 과 elapsedCompletedTime 의 합을 계산
            else {
                taskMeasures.setTotalElapsedTime(taskMeasures.getElapsedPausedTime() + elapsedCompletedTime);
            }
        }
        // 설정 저장
        taskMeasuresRepository.save(taskMeasures);
    }
}
