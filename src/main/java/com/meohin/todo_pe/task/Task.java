package com.meohin.todo_pe.task;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

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

    @Column(unique = true)
    private Long estimatedAt;

    private LocalDateTime createdAt;

    private LocalDateTime modifiedAt;
}
