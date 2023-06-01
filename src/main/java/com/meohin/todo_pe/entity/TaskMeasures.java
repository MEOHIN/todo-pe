package com.meohin.todo_pe.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Duration;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
public class TaskMeasures {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime startTime;

    private LocalDateTime completeTime;

    private Duration elapsedTime;

    @ManyToOne
    @JoinColumn(unique = false)
    private Task task;

    @ManyToOne
    private User user;
}