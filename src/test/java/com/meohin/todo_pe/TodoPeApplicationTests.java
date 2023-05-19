package com.meohin.todo_pe;

import com.meohin.todo_pe.measure.TaskMeasures;
import com.meohin.todo_pe.measure.TaskMeasuresRepository;
import com.meohin.todo_pe.task.Task;
import com.meohin.todo_pe.task.TaskRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Duration;
import java.time.LocalDateTime;

@SpringBootTest
class TodoPeApplicationTests {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private TaskMeasuresRepository taskMeasuresRepository;

    @Test
    void testJPA() {

        Task t1 = new Task();   // Task 엔티티 객체 생성
        t1.setSubject("200p 한국어 비문학 읽기");
        t1.setDescription("매일 5페이지 읽기");
        t1.setEstimatedAt(Duration.ofHours(10).toMillis());
        t1.setCreatedAt(LocalDateTime.of(2023, 5, 1, 19, 0, 0));
        t1.setModifiedAt(LocalDateTime.of(2023, 5, 9, 7, 30, 0));
        this.taskRepository.save(t1);   // 첫 번째 태스크 저장

        Task t2 = new Task();
        t2.setSubject("영상 편집");
        t2.setDescription("미오인 브이로그 하루 3시간 편집");
        t1.setEstimatedAt(Duration.ofHours(5).plusMinutes(30).toMillis());
        t2.setCreatedAt(LocalDateTime.of(2023, 5, 2, 17, 20, 0));
        this.taskRepository.save(t2);

        Task t3 = new Task();
        t3.setSubject("21일 습관 만들기");
        t3.setDescription("매일 20분");
        t1.setEstimatedAt(Duration.ofHours(10).plusMinutes(30).toMillis());
        t3.setCreatedAt(LocalDateTime.of(2023, 5, 4, 21, 5, 0));
        this.taskRepository.save(t3);


        TaskMeasures tm1 = new TaskMeasures();   // TaskMeasures 엔티티 객체 생성
        tm1.setStartTime(LocalDateTime.of(2023, 5, 2, 8, 0, 0));
        tm1.setCompleteTime(LocalDateTime.of(2023, 5, 8, 9, 0, 0));
        tm1.setElapsedTime(Duration.ofHours(9).ofMinutes(10));
        tm1.setTask(t2);
        this.taskMeasuresRepository.save(tm1);

        TaskMeasures tm2 = new TaskMeasures();
        tm2.setStartTime(LocalDateTime.of(2023, 5, 3, 8, 20, 0));
        tm2.setElapsedTime(Duration.ofHours(7).ofMinutes(25));
        tm2.setTask(t1);
        this.taskMeasuresRepository.save(tm2);

        TaskMeasures tm3 = new TaskMeasures();
        tm3.setStartTime(LocalDateTime.of(2023, 5, 9, 8, 30, 0));
        tm3.setCompleteTime(LocalDateTime.of(2023, 5, 22, 9, 10, 0));
        tm3.setElapsedTime(Duration.ofHours(10).ofMinutes(5));
        tm3.setTask(t2);
        this.taskMeasuresRepository.save(tm3);

        TaskMeasures tm4 = new TaskMeasures();
        tm4.setStartTime(LocalDateTime.of(2023, 5, 5, 7, 0, 0));
        tm4.setElapsedTime(Duration.ofHours(3).ofMinutes(45));
        tm4.setTask(t3);
        this.taskMeasuresRepository.save(tm4);
    }
}
