package com.meohin.todo_pe.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
public class TaskMeasures {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer estimatedAt;

    private LocalDateTime startTime;

    private LocalDateTime pauseTime;

    private LocalDateTime continueTime;

    private LocalDateTime completeTime;

    private Integer elapsedPausedTime;

    private Integer elapsedCompletedTime;

    private Integer totalElapsedTime;

    @ManyToOne
    @JoinColumn(unique = false)
    private Task task;

    @ManyToOne
    private SiteUser siteUser;
}