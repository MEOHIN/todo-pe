package com.meohin.todo_pe.repository;

import com.meohin.todo_pe.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task, Integer> {
}
