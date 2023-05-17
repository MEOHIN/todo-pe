package com.meohin.todo_pe.task;

import jakarta.persistence.*;

import java.time.Duration;
import java.time.LocalDateTime;

@Entity
public class TaskMeasures {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime startTime;

    private LocalDateTime completeTime;

    private Duration elapsedTime;

    @ManyToOne
    private Task task;
}