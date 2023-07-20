package com.meohin.todo_pe.repository;

import com.meohin.todo_pe.entity.TaskMeasures;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskMeasuresRepository extends JpaRepository<TaskMeasures, Long> {

    TaskMeasures findByTaskIdAndCompleteTimeNull(Long taskId);

    List<TaskMeasures> findByTaskIdAndCompleteTimeNotNull(Long taskId);

    List<TaskMeasures> findByTaskId(Long taskId);
}
