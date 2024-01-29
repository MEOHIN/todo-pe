package com.meohin.todo_pe.repository;

import com.meohin.todo_pe.entity.Task;
import com.meohin.todo_pe.entity.TaskWithMeasures;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TaskWithMeasuresRepository extends JpaRepository<Task, Long> {

    // task와 그 task에 딸린 taskMeasures 데이터를 최신 순으로 정렬
    @Query(value = """
        SELECT
            t1.id as taskId,
            t1.subject as subject,
            t1.description as description,
            t1.estimated_at as estimatedAt,
            t1.status as status,
            tm.id as measuresId,
            tm.start_time as startTime,
            tm.pause_time as pauseTime,
            tm.continue_time as continueTime,
            tm.elapsed_paused_time as elapsedTime
        FROM (
            SELECT t.*, MAX(tm.id) as tm_id
            FROM task t
            LEFT JOIN task_measures tm
            on t.id = tm.task_id
            WHERE t.site_user_id = :siteUserId
            GROUP BY t.id
        ) as t1
        LEFT JOIN task_measures tm
        on tm.id = tm_id
        ORDER BY tm.start_time DESC;
    """, nativeQuery = true)
    List<TaskWithMeasures> getTaskListWithMeasures(@Param("siteUserId") Long siteUserId);

    // 주어진 subject를 포함하고 특정 사용자에게 속한 Task 엔티티 목록을 반환한다.
    @Query(value = """
        SELECT
            t1.id as taskId,
            t1.subject as subject,
            t1.description as description,
            t1.estimated_at as estimatedAt,
            t1.status as status,
            tm.id as measuresId,
            tm.start_time as startTime,
            tm.pause_time as pauseTime,
            tm.continue_time as continueTime,
            tm.elapsed_paused_time as elapsedTime
        FROM (
            SELECT t.*, MAX(tm.id) as tm_id
            FROM task t
            LEFT JOIN task_measures tm
            on t.id = tm.task_id
            WHERE t.site_user_id = :siteUserId
            AND t.subject LIKE %:subject%
            GROUP BY t.id
        ) as t1
        LEFT JOIN task_measures tm
        on tm.id = tm_id
        ORDER BY tm.start_time DESC;                                
        """,
            nativeQuery = true)
    List<TaskWithMeasures> getTasksWithMeasuresBySubject(@Param("subject") String subject, @Param("siteUserId") Long siteUserId);
}