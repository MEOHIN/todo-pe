package com.meohin.todo_pe.entity;

import com.meohin.todo_pe.TaskStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Entity
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 200)
    private String subject;

    @Column(columnDefinition = "TEXT")
    private String description;

    private Integer estimatedAt;

    private LocalDateTime createdAt;

    private LocalDateTime modifiedAt;

    private TaskStatus status;

    @OneToMany(mappedBy = "task")
    private List<TaskMeasures> taskMeasuresList;

    @ManyToOne
    private User user;
}
