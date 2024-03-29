package com.meohin.todo_pe.entity;

import java.time.LocalDateTime;

public interface TaskWithMeasures {
    Long getTaskId();
    String getSubject();
    String getDescription();
    Integer getEstimatedAt();
    TaskStatus getStatus();

    Long getMeasuresId();
    LocalDateTime getStartTime();
    LocalDateTime getPauseTime();
    LocalDateTime getContinueTime();
    Integer getElapsedTime();
}
