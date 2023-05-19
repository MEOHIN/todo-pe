package com.meohin.todo_pe;

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
    }
}
