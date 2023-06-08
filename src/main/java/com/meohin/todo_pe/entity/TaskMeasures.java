package com.meohin.todo_pe.entity;

import com.meohin.todo_pe.TaskStatus;
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

    private LocalDateTime startTime;

    private LocalDateTime completeTime;

    private Integer elapsedTime;

    private TaskStatus status;

    public void start() {
        if (status == TaskStatus.STANDBY || status == TaskStatus.PAUSE) {
            status = TaskStatus.ING;
        }
    }

    public void stop() {
        if (status == TaskStatus.ING) {
            status = TaskStatus.PAUSE;
        }
    }

    public void reset() {
        elapsedTime = 0;
        status = TaskStatus.STANDBY;
    }

    @ManyToOne
    @JoinColumn(unique = false)
    private Task task;

    @ManyToOne
    private User user;
}