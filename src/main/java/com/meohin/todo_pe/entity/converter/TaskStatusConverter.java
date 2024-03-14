package com.meohin.todo_pe;

import com.meohin.todo_pe.entity.TaskStatus;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class TaskStatusConverter implements AttributeConverter<TaskStatus, String> {

    @Override
    public String convertToDatabaseColumn(TaskStatus status) {
        return status.getValue();
    }

    @Override
    public TaskStatus convertToEntityAttribute(String dbData) {
        return TaskStatus.ofCode(dbData);
    }
}
