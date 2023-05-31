package com.meohin.todo_pe;

import com.meohin.todo_pe.entity.TaskMeasures;
import com.meohin.todo_pe.repository.TaskMeasuresRepository;
import com.meohin.todo_pe.entity.Task;
import com.meohin.todo_pe.repository.TaskRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class TodoPeApplicationTests {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private TaskMeasuresRepository taskMeasuresRepository;

    @Test
    void testJPA() {

        List<Task> allTasks = this.taskRepository.findAll();
        assertEquals(3, allTasks.size());

        List<TaskMeasures> allMeasures = this.taskMeasuresRepository.findAll();
        assertEquals(4, allMeasures.size());

        Task t = allTasks.get(0);
        assertEquals("200p 한국어 비문학 읽기", t.getSubject());

        TaskMeasures tm = allMeasures.get(2);
        assertEquals(2, tm.getTask().getId());
    }
}
