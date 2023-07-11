package com.meohin.todo_pe.repository;

import com.meohin.todo_pe.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findTasksBySubjectContains(String subject);
}
