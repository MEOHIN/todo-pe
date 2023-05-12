package com.meohin.todo_pe.task;

import jakarta.persistence.*;

import java.time.Duration;
import java.time.LocalDateTime;

@Entity
public class TaskMeasures {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private LocalDateTime startTime;

    @Column(unique = true)
    private LocalDateTime completeTime;

    @Column(unique = true)
    private Duration elapsedTime;

    @ManyToOne
    private Task task;
}