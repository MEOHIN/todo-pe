package com.meohin.todo_pe.repository;

import com.meohin.todo_pe.entity.TaskMeasures;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskMeasuresRepository extends JpaRepository<TaskMeasures, Long> {

    TaskMeasures findByTaskIdAndCompleteTimeNull(Long taskId);
}
