package com.meohin.todo_pe.dao;

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
}
