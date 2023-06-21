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
    // 조건문
    //  설정하려는 시각이 pause 시각이면
    //      pauseTime 설정
    //      elapsedPausedTime 설정
    //  설정하려는 시각이 continue 시각이면
    //      continueTime 설정
    //  설정하려는 시각이 complete 시각면
    //      조건문
    //          continueTime 이 있으면
    //              completeTime 설정
    //              elapsedCompletedTime 설정
    //                  : continueTime부터 completeTime까지
    //              totalElapsedTime 설정
    //                  : elapsedPausedTime + elapsedCompletedTime
    //          continueTime이 없으면
    //              completeTime 설정
    //              totalElapsedTime 설정
    //                  : startTime부터 completeTime까지
    //  세팅 저장
}
