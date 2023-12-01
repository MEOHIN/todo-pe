package com.meohin.todo_pe.repository;

import com.meohin.todo_pe.entity.TaskMeasures;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskMeasuresRepository extends JpaRepository<TaskMeasures, Long> {
    // // 주어진 taskId를 가진 TaskMeasures 엔티티 중 완료시각이 null인 엔티티를 반환한다.
    TaskMeasures findByTaskIdAndCompleteTimeNull(Long taskId);

    // 주어진 taskId를 가진 TaskMeasures 엔티티 중 완료시각이 null이 아닌 엔티티 목록을 반환한다.
    List<TaskMeasures> findByTaskIdAndCompleteTimeNotNull(Long taskId);

    // 주어진 taskId를 가진 TaskMeasures 엔티티 목록을 반환한다.
    List<TaskMeasures> findByTaskId(Long taskId);
}
