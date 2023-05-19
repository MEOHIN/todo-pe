package com.meohin.todo_pe.task;

import com.meohin.todo_pe.measure.TaskMeasures;
import com.meohin.todo_pe.user.User;
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
    private Integer id;

    @Column(length = 200)
    private String subject;

    @Column(columnDefinition = "TEXT")
    private String description;

    private Long estimatedAt;

    private LocalDateTime createdAt;

    private LocalDateTime modifiedAt;

    @OneToMany(mappedBy = "task")
    private List<TaskMeasures> taskMeasuresList;

    @ManyToOne
    private User user;
}
