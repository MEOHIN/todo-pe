package com.meohin.todo_pe.repository;

import com.meohin.todo_pe.entity.Task;
import com.meohin.todo_pe.entity.TaskWithMeasures;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TaskWithMeasuresRepository extends JpaRepository<Task, Long> {

    @Query(value = """
        SELECT
            t1.id as taskId,
            t1.subject as subject,
            t1.description as description,
            t1.estimated_at as estimatedAt,
            t1.status as status,
            tm.id as measuresId,
            tm.start_time as startTime,
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
}