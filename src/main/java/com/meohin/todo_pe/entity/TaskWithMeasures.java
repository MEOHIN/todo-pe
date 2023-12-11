package com.meohin.todo_pe.entity;

import com.meohin.todo_pe.TaskStatus;

import java.time.LocalDateTime;

public interface TaskWithMeasures {
    Long getTaskId();
    String getSubject();
    String getDescription();
    Integer getEstimatedAt();
    TaskStatus getStatus();

    Long getMeasuresId();
    LocalDateTime startTime();
    Integer getElapsedTime();
}
