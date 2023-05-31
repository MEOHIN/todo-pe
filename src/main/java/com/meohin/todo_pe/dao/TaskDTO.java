package com.meohin.todo_pe.dao;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TaskDTO {
    private Integer id;
    private String subject;
    private String description;
    private Long estimatedAt;
}
