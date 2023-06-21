package com.meohin.todo_pe.service;

import com.meohin.todo_pe.TaskStatus;
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
            //      조건에 따라 totalElapsedTime 설정하는 메서드 호출
            calculateTime(taskMeasures);
        }

        // 설정 저장
        taskMeasuresRepository.save(taskMeasures);
    }

    // 시간을 계산하는 메서드
    // 리턴 타입: void
    // 파라미터: TaskMeasures
    // elapsedPausedTime 설정
    //  : startTime 과 pauseTime 사이의 시간 간격을 계산
    // elapsedCompletedTime 설정
    //  : continueTime 과 completeTime 사이의 시간 간격을 계산
    // totalElapsedTime 설정
    // 조건문
    //  continueTime 이 없으면 startTime 과 completeTime 사이의 시간 간격을 계산
    //  continueTime 이 있으면 elapsedPausedTime 과 elapsedCompletedTime 의 합을 계산
    // 설정 저장
}
