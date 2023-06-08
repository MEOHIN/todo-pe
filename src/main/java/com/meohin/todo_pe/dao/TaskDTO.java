package com.meohin.todo_pe.dao;

import com.meohin.todo_pe.TaskStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class TaskDTO {
    private Long id;
    private String subject;
    private String description;
    private Integer estimatedAt;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private TaskStatus taskStatus;
}
